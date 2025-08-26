package com.example.FruitApp.service;

import com.example.FruitApp.model.Fruits;
import com.example.FruitApp.model.Sebet;
import com.example.FruitApp.model.Statuses;
import com.example.FruitApp.model.User;
import com.example.FruitApp.repository.FruitsRepository;
import com.example.FruitApp.repository.SebetRepository;
import com.example.FruitApp.repository.StatusRepository;
import com.example.FruitApp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
@RequiredArgsConstructor
public class SebetService {

    private final SebetRepository sebetRepository;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final FruitsRepository fruitRepository;
    private final StatusRepository statusRepository;


    public Page<Sebet> getUserBasket(String token, int page, int size) {
        UUID userId = UUID.fromString(jwtService.extractUserId(token));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User tapilmadi"));
        Pageable pageable = PageRequest.of(page, size);
        return sebetRepository.findByUserId(user, pageable);
    }

    public List<Sebet> getAllSebet(UUID userId) {
        return sebetRepository.findAllByUserId_Id(userId);
    }


    public int getUserBasketCount(String token) {
        String userId = jwtService.extractUserId(token);
        List<Sebet> sebet = sebetRepository.findAllByUserId_Id(UUID.fromString(userId));
        return sebet.size();
    }


    public Object addToSebet(String token, UUID mehsulId) {
        String userIdStr = jwtService.extractUserId(token);
        UUID userId = UUID.fromString(userIdStr);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User tapılmadı"));

        Fruits fruit = fruitRepository.findById(mehsulId)
                .orElseThrow(() -> new RuntimeException("Məhsul tapılmadı"));

        Statuses defaultStatus = statusRepository.findByName("ACTIVE")
                .orElseThrow(() -> new RuntimeException("Default status not found"));

        var existingSebet = sebetRepository.findByUserIdAndFruitId(user, fruit);
        if (existingSebet.isPresent()) {
            Sebet sebet = existingSebet.get();
            sebet.setSay(sebet.getSay() + 1);
            sebetRepository.save(sebet);
            return "Bu mehsul artiq sebetde var ve məhsulun sayı artırıldı";
        }

        Sebet sebet = new Sebet();
        sebet.setUserId(user);
        sebet.setFruitId(fruit);
        sebet.setSay(1);
        sebet.setStatusId(defaultStatus);

        sebetRepository.save(sebet);

        return ResponseEntity.ok("Məhsul səbətə əlavə edildi");
    }


    public Sebet increaseProductCount(UUID sebetId) {
        Sebet sebet = sebetRepository.findById(sebetId)
                .orElseThrow(() -> new RuntimeException("Sebet tapılmadı"));

        int availableStock = sebet.getFruitId().getMiqdar();

        if (sebet.getSay() < availableStock) {
            sebet.setSay(sebet.getSay() + 1);
            return sebetRepository.save(sebet);
        } else {
            throw new RuntimeException("Stokda kifayət qədər məhsul yoxdur");
        }
    }


    public Sebet decreaseProductCount(UUID sebetId) {
        Sebet sebet = sebetRepository.findById(sebetId)
                .orElseThrow(() -> new RuntimeException("Sebet tapılmadı"));

        if (sebet.getSay() <= 1) {
            throw new RuntimeException("Məhsul sayı 1-dən aşağı düşə bilməz");
        }

        sebet.setSay(sebet.getSay() - 1);
        return sebetRepository.save(sebet);
    }


    public Map<String, Double> getUserBasketTotalPrice(UUID userId) {
        List<Sebet> userBasket = sebetRepository.findAllByUserId_Id(userId);

        double totalPrice = userBasket.stream()
                .mapToDouble(item -> item.getFruitId().getQiymet() * item.getSay())
                .sum();

        Map<String, Double> response = new HashMap<>();
        response.put("totalPrice", totalPrice);

        return response;
    }


    public List<String> validateUserBasket(UUID userId) {
        List<Sebet> sebetList = sebetRepository.findAllByUserId_Id(userId);

        List<String> errors = new ArrayList<>();

        for (Sebet sebet : sebetList) {
            Fruits fruit = sebet.getFruitId();
            int requestedCount = sebet.getSay();
            int stockCount = fruit.getMiqdar();

            if (requestedCount > stockCount) {
                errors.add("Sebətdə '" + fruit.getName() +
                        "' məhsulunun sayı stokdan artıqdır! (Sebetdə: "
                        + requestedCount + ", Stokda: " + stockCount + ")");
            }
        }

        if (errors.isEmpty()) {
            errors.add("Sebet düzgündür ✔️");
        }

        return errors;
    }


    public String deleteSebetFruitsById(UUID id) {
        if (!sebetRepository.existsById(id)) {
            return "Bu ID: " + id + " uzre mehsul sebetde yoxdur!";
        }

        sebetRepository.deleteById(id);
        return "Mehsul sebetden silindi!";
    }

}

