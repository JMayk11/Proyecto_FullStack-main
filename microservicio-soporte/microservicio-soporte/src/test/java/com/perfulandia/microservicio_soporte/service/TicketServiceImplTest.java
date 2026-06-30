package com.perfulandia.microservicio_soporte.service;

import com.perfulandia.microservicio_soporte.clients.InventarioClient;
import com.perfulandia.microservicio_soporte.clients.NotificacionesClient;
import com.perfulandia.microservicio_soporte.entities.Ticket;
import com.perfulandia.microservicio_soporte.repositories.TicketRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class TicketServiceImplTest {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private InventarioClient inventarioClient;

    @Mock
    private NotificacionesClient notificacionesClient;

    @InjectMocks
    private TicketServiceImpl ticketService;

    @Test
    public void testAbrirTicket_PrioridadAlta() {
        Ticket ticket = new Ticket();
        ticket.setPedidoId(10L);
        ticket.setTipoUrgencia("PRODUCTO_DAÑADO");

        Mockito.when(ticketRepository.save(Mockito.any(Ticket.class))).thenAnswer(i -> i.getArguments()[0]);

        Ticket resultado = ticketService.abrirTicket(ticket);
        Assertions.assertEquals("ALTA", resultado.getPrioridad());
        Assertions.assertEquals("ABIERTO", resultado.getEstado());
    }

    @Test
    public void testAbrirTicket_PrioridadMedia() {
        Ticket ticket = new Ticket();
        ticket.setPedidoId(10L);
        ticket.setTipoUrgencia("CAMBIO_FRAGANCIA");

        Mockito.when(ticketRepository.save(Mockito.any(Ticket.class))).thenAnswer(i -> i.getArguments()[0]);

        Ticket resultado = ticketService.abrirTicket(ticket);
        Assertions.assertEquals("MEDIA", resultado.getPrioridad());
    }

    @Test
    public void testAbrirTicket_PrioridadBaja() {
        Ticket ticket = new Ticket();
        ticket.setPedidoId(10L);
        ticket.setTipoUrgencia("OTRO_MOTIVO");

        Mockito.when(ticketRepository.save(Mockito.any(Ticket.class))).thenAnswer(i -> i.getArguments()[0]);

        Ticket resultado = ticketService.abrirTicket(ticket);
        Assertions.assertEquals("BAJA", resultado.getPrioridad());
    }

    @Test
    public void testAbrirTicket_PedidoInvalido_Exception() {
        Ticket ticket = new Ticket();
        ticket.setPedidoId(null);

        Assertions.assertThrows(IllegalArgumentException.class, () -> ticketService.abrirTicket(ticket));
    }

    @Test
    public void testAbrirTicket_UrgenciaVacia_Exception() {
        Ticket ticket = new Ticket();
        ticket.setPedidoId(10L);
        ticket.setTipoUrgencia("");

        Assertions.assertThrows(IllegalArgumentException.class, () -> ticketService.abrirTicket(ticket));
    }

    @Test
    public void testProcesarDevolucion_Exitoso() {
        Ticket ticketMock = new Ticket();
        ticketMock.setId(1L);
        ticketMock.setEstado("ABIERTO");

        Mockito.when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticketMock));
        Mockito.when(ticketRepository.save(Mockito.any(Ticket.class))).thenAnswer(i -> i.getArguments()[0]);

        Ticket resultado = ticketService.procesarDevolucionFisica(1L, 5L, 2);
        Assertions.assertEquals("EN_PROCESO", resultado.getEstado());
        Mockito.verify(inventarioClient, Mockito.times(1)).reingresarStockFisico(5L, 2);
    }

    @Test
    public void testProcesarDevolucion_CantidadInvalida_Exception() {
        Ticket ticketMock = new Ticket();
        Mockito.when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticketMock));

        Assertions.assertThrows(IllegalArgumentException.class, () -> ticketService.procesarDevolucionFisica(1L, 5L, 0));
    }

    @Test
    public void testCerrarTicket_Exitoso_ConNotificacionOk() {
        Ticket ticketMock = new Ticket();
        ticketMock.setId(1L);
        ticketMock.setPedidoId(1024L);

        Mockito.when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticketMock));
        Mockito.when(ticketRepository.save(Mockito.any(Ticket.class))).thenAnswer(i -> i.getArguments()[0]);

        Ticket resultado = ticketService.cerrarTicket(1L);
        Assertions.assertEquals("CERRADO", resultado.getEstado());
        Mockito.verify(notificacionesClient, Mockito.times(1)).enviarCorreo(Mockito.anyMap());
    }

    @Test
    public void testCerrarTicket_ConNotificacionError_IgualGuarda() {
        Ticket ticketMock = new Ticket();
        ticketMock.setId(1L);
        ticketMock.setPedidoId(1024L);

        Mockito.when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticketMock));
        Mockito.doThrow(new RuntimeException("Servicio de red caído")).when(notificacionesClient).enviarCorreo(Mockito.anyMap());
        Mockito.when(ticketRepository.save(Mockito.any(Ticket.class))).thenAnswer(i -> i.getArguments()[0]);

        Ticket resultado = ticketService.cerrarTicket(1L);
        Assertions.assertEquals("CERRADO", resultado.getEstado());
    }
}