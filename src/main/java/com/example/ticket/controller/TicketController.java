package com.example.ticket.controller;

import com.example.ticket.model.entity.ApiResponse;
import com.example.ticket.model.entity.Ticket;
import com.example.ticket.model.request.RequestTicket;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/v1/tickets")
public class TicketController {
    List<Ticket> ticketList = new ArrayList<>();
    public TicketController() {
        ticketList.add(new Ticket(1,"Visal",LocalDate.now(),"Moon","Sun",23.9,true,"BOOKED",27));
        ticketList.add(new Ticket(2,"Koko",LocalDate.now(),"Moon","Sun",23.9,true,"CANCELED",27));
        ticketList.add(new Ticket(3,"Lolo",LocalDate.now(),"Moon","Sun",23.9,true,"BOOKED",27));
        ticketList.add(new Ticket(4,"Visal",LocalDate.now(),"Moon","Sun",23.9,true,"COMPLETED",27));
        ticketList.add(new Ticket(5,"Koko",LocalDate.now(),"Moon","Sun",23.9,true,"BOOKED",27));
        ticketList.add(new Ticket(6,"Lolo",LocalDate.now(),"Moon","Sun",23.9,true,"CANCELED",27));
        ticketList.add(new Ticket(7,"Visal",LocalDate.now(),"Moon","Sun",23.9,true,"BOOKED",27));
        ticketList.add(new Ticket(8,"Koko",LocalDate.now(),"Moon","Sun",23.9,true,"COMPLETED",27));
        ticketList.add(new Ticket(9,"Lolo",LocalDate.now(),"Moon","Sun",23.9,true,"CANCELED",27));
        ticketList.add(new Ticket(10,"Visal",LocalDate.now(),"Moon","Sun",23.9,true,"COMPLETED",27));
        ticketList.add(new Ticket(11,"Koko",LocalDate.now(),"Moon","Sun",23.9,true,"BOOKED",27));
        ticketList.add(new Ticket(12,"Lolo",LocalDate.now(),"Moon","Sun",23.9,true,"COMPLETED",27));
    }
    @Operation(summary = "Add ticket")
    @PostMapping
    public List<Ticket> addTicket(@RequestBody Ticket ticket){
        ticket.setTicketId(ticket.getTicketId()+1);
        ticketList.add(ticket);
        return ticketList;
    }

    @Operation(summary = "Show Tickets")
    @GetMapping
    public ResponseEntity<ApiResponse<List<Ticket>>> showTickets(@RequestParam int page, @RequestParam int pageSize){
        if(page<1 || pageSize<1){
            return null;
        }else{
            List<Ticket> data = ticketList.stream().skip(page).limit(pageSize).toList();
            ApiResponse<List<Ticket>> response = new ApiResponse<>(true,"All tickets retrieved successfully.",HttpStatus.OK ,data,LocalDateTime.now());
            return ResponseEntity.ok(response);
        }
    }
//    public List<Ticket> getTickets(@RequestParam int page, @RequestParam int pageSize){
//        if(page<1 || pageSize<1){
//            return null;
//        }else{
//            return ticketList.stream().skip(page).limit(pageSize).toList();
//        }
//    }

    @Operation(summary = "Show Ticket by ID")
    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<Ticket>> showTicketById(@PathVariable int id){
        if(id<0 || id>ticketList.size()){
             ApiResponse<Ticket> error = new ApiResponse<>(false,"Ticket retrieved failed.",HttpStatus.NOT_FOUND,null,LocalDateTime.now())  ;
             return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }else {
            Ticket ticket = ticketList.get(id-1);
            ApiResponse<Ticket> response = new ApiResponse<>(true,"Ticket retrieved successfully.",HttpStatus.OK,ticket,LocalDateTime.now());
            return ResponseEntity.ok(response);
        }
    }
//    public Ticket getTicketId(@PathVariable int id){
//        if(id<0 || id>ticketList.size()){
//            return null;
//        }else {
//            return ticketList.get(id-1);
//        }
//    }

    @Operation(summary = "Search by Name")
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<Ticket>>> showTicketsByName(@RequestParam String name){
        List<Ticket> foundTicket = new ArrayList<>();
        if(name.isBlank()){
            ApiResponse<List<Ticket>> error = new ApiResponse<>(true,"Tickets searched failed.",HttpStatus.NOT_FOUND,null,LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }else {
            for(Ticket findTicket : ticketList){
                if(findTicket.getPassengerName().toLowerCase().contains(name.toLowerCase())){
                    foundTicket.add(findTicket);
                }
            }
            ApiResponse<List<Ticket>> response = new ApiResponse<>(true,"Tickets searched successfully.",HttpStatus.OK,foundTicket,LocalDateTime.now());
            return ResponseEntity.ok(response);
        }
    }
//    public List<Ticket> searchTicketName(@RequestParam String name){
//        List<Ticket> foundTicket = new ArrayList<>();
//        for(Ticket findTicket : ticketList){
//            if(findTicket.getPassengerName().toLowerCase().contains(name.toLowerCase())){
//                foundTicket.add(findTicket);
//            }
//        }
//        return foundTicket;
//    }

    @Operation(summary = "Update Ticket")
    @PutMapping("{id}")
    public String updateTicket(@PathVariable int id,@RequestBody RequestTicket updatedTicket){
        if(id<0 || id>ticketList.size()){
            return null;
        }else {
            for(Ticket findTicket : ticketList){
                if(findTicket.getTicketId() == id){
                    findTicket.setPassengerName(updatedTicket.getPassengerName());
                    findTicket.setTravelDate(updatedTicket.getTravelDate());
                    findTicket.setSourceStation(updatedTicket.getSourceStation());
                    findTicket.setDestinationStation(updatedTicket.getDestinationStation());
                    findTicket.setTicketStatus(updatedTicket.getTicketStatus());
                    findTicket.setPaymentStatus(updatedTicket.isPaymentStatus());
                }
            }
        }
        return "Updated successfully";
    }

    @Operation(summary = "Delete Ticket")
    @DeleteMapping("{id}")
    public String deleteTicket(@PathVariable int id){
        if(id<0 || id>ticketList.size()){
            return null;
        }else {
            for(Ticket findTicket : ticketList){
                if(findTicket.getTicketId()== id){
                    ticketList.remove((findTicket));
                    return "Deleted successfully";
                }
            }
            return "Deleted failed";
        }
    }
    public enum Option{
        BOOKED,CANCELED, COMPLETED
    }
    @Operation(summary = "Filter Tickets")
    @GetMapping("/filter")
    public List<Ticket> filterTickets(@RequestParam Option ticketStatus, @RequestParam LocalDate travelDate){
       return ticketList.stream().filter(data-> (data.getTicketStatus().equalsIgnoreCase(String.valueOf(ticketStatus)) && data.getTravelDate().equals(travelDate))).toList();
    }
}

