package ru.geekbrains.supershop.services.soap;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.geekbrains.paymentservice.*;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PriceService {

    public List<Product> getProducts(int price) {

        PaymentPort paymentPort = new PaymentPortService().getPaymentPortSoap11();

        GetProductRequest request = new GetProductRequest();
        request.setPrice(price);
        GetProductResponse response;

        try {
            response = paymentPort.getProduct(request);
        } catch (Exception ex) {
            response = null;
        }

        return response == null ? new ArrayList<>() : response.getProducts();

    }

}