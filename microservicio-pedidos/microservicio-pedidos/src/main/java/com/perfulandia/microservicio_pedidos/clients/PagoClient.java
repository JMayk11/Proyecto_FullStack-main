package com.perfulandia.microservicio_pedidos.clients; // <── ¡REVISA QUE DIGA PEDIDOS Y NO PAGOS!

import com.perfulandia.microservicio_pedidos.entities.PagoModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "microservicio-pagos")
public interface PagoClient { // <── ¡REVISA QUE SE LLAME EXACTAMENTE PagoClient!

    @PostMapping("/pagos/procesar")
    PagoModel procesarPago(@RequestBody PagoModel pago);
}