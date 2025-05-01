//package id.ac.ui.cs.advprog.hiringgo.manajemenakun.controller;
//
//import id.ac.ui.cs.advprog.hiringgo.manajemenakun.service.UserAccountDTO;
//import id.ac.ui.cs.advprog.hiringgo.manajemenakun.service.UserManagementService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.ui.Model;
//
//import java.util.Arrays;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//public class UserManagementControllerTest {
//
//    @Mock
//    private UserManagementService userManagementService;
//
//    @Mock
//    private Model model;
//
//    private UserManagementController controller;
//
//    @BeforeEach
//    public void setup() {
//        MockitoAnnotations.openMocks(this);
//        controller = new UserManagementController(userManagementService);
//    }
//
//    @Test
//    public void listUsersShouldAddAccountsToModelAndReturnView() {
//        List<UserAccountDTO> accounts = Arrays.asList(
//                new UserAccountDTO("1", "-", "admin@example.com", "Admin"),
//                new UserAccountDTO("2", "Nama Dosen", "dosen@example.com", "Dosen"),
//                new UserAccountDTO("3", "Nama Mahasiswa", "mahasiswa@example.com", "Mahasiswa")
//        );
//
//        when(userManagementService.getAllUserAccounts()).thenReturn(accounts);
//
//        String viewName = controller.listUsers(model);
//
//        assertEquals("user/list", viewName);
//        verify(model).addAttribute("userAccounts", accounts);
//        verify(userManagementService).getAllUserAccounts();
//    }
//
//    @Test
//    public void listUsersShouldHandleEmptyList() {
//        List<UserAccountDTO> emptyList = List.of();
//        when(userManagementService.getAllUserAccounts()).thenReturn(emptyList);
//
//        String viewName = controller.listUsers(model);
//
//        assertEquals("user/list", viewName);
//        verify(model).addAttribute("userAccounts", emptyList);
//    }
//
//    @Test
//    public void listUsersShouldHandleError() {
//        when(userManagementService.getAllUserAccounts()).thenThrow(new RuntimeException("Database error"));
//
//        String viewName = controller.listUsers(model);
//
//        assertEquals("error", viewName);
//        verify(model).addAttribute(eq("errorMessage"), anyString());
//    }
//}