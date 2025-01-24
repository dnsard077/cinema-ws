package com.example.cinema.cinemaws.service;

import com.example.cinema.cinemaws.model.Subscription;
import com.example.cinema.cinemaws.model.User;
import com.example.cinema.cinemaws.repository.UserRepository;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

class ReportServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ReportService reportService;

    ReportServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void givenUsers_whenExportUserToExcel_thenReturnExcelFile() throws IOException {
        // Given
        User user1 = new User();
        user1.setUserId(1L);
        user1.setUsername("john_doe");
        user1.setEmail("john.doe@example.com");
        user1.setRole(User.Role.CUSTOMER);
        user1.setSubscriptions(Collections.singletonList(new Subscription()));

        User user2 = new User();
        user2.setUserId(2L);
        user2.setUsername("jane_doe");
        user2.setEmail("jane.doe@example.com");
        user2.setRole(User.Role.ADMIN);
        user2.setSubscriptions(Collections.emptyList());

        List<User> users = Arrays.asList(user1, user2);
        when(userRepository.findAll()).thenReturn(users);

        // When
        ByteArrayInputStream result = reportService.exportUserToExcel();

        // Then
        assertNotNull(result);
        try (Workbook workbook = new XSSFWorkbook(result)) {
            assertNotNull(workbook.getSheet("Users"));
        }
    }
}
