package limpo.orderservice.client.controller;

import limpo.orderservice.client.service.ClientService;
import limpo.orderservice.client.dto.Client;
import limpo.orderservice.client.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ClientController.BASE_URL)
public class ClientController {

    public static final String BASE_URL = "/api/v1/clients";

    @Autowired
    private ClientRepository repository;

    @Autowired
    private ClientService clientService;

    @GetMapping
    public ResponseEntity<?> getAllClients() {
        List<Client> result = clientService.getAllClients();

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getClient(@PathVariable("id") Long id) {
        Client result = clientService.getClientById(id);
        if (result == null) {
            return new ResponseEntity<>("Client with " + id + " cannot be found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<?> addNewClient(@RequestBody Client client) {
        Client result = clientService.createClient(client);

        if (result== null) {
            return new ResponseEntity<>("Client with this email already exists", HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateClient(@PathVariable Long id, @RequestBody Client client) {
        Client result = clientService.updateClient(id, client);
        if (result == null) {
            return new ResponseEntity<>("Client with id " + id + " cannot be found", HttpStatus.NOT_FOUND);
        }
        if (result.getEmail() == null) {
            return new ResponseEntity<>("Client with this email or phone number already exists", HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteClient(@PathVariable Long id) {
        Long result = clientService.deleteClient(id);
        if (result == null) {
            return new ResponseEntity<>("Client with id " + id + " cannot be found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}