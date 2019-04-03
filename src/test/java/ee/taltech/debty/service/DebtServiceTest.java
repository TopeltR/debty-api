package ee.taltech.debty.service;

import ee.taltech.debty.entity.Debt;
import ee.taltech.debty.entity.Person;
import ee.taltech.debty.repository.DebtRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DebtServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private DebtRepository debtRepository;

    @Spy
    @InjectMocks
    private DebtService debtService;

    private Person person = Person.builder().firstName("Bob").lastName("Builder")
            .id(3L).build();

    @Test
    public void saveDebt_savesDebt() {
        Debt debt = new Debt();
        debt.setId(1L);
        debt.setTitle("Test debt");
        debt.setPayer(person);
        debtService.saveDebt(debt);
        verify(debtRepository, times(1)).save(debt);
    }

    @Test
    public void saveDebts_savesAllDebts() {
        Debt debt = new Debt();
        debt.setId(1L);
        debt.setTitle("Test debt");
        debt.setPayer(person);

        Debt debt1 = new Debt();
        debt.setId(2L);
        debt.setTitle("Test debt2");

        debtService.saveDebts(Arrays.asList(debt, debt1));
        assertNotNull(debt.getModifiedAt());
        verify(debtRepository).saveAll(Arrays.asList(debt, debt1));
    }

    @Test
    public void getAllDebtsByUserId_whenNoUser_returnsEmptyArrayList() {
        when(userService.getUserById(3L)).thenReturn(Optional.empty());

        List<Debt> debtList = debtService.getAllDebtsByUserId(3L);

        verify(debtRepository, never()).findAllByPersonParticipating(person);
        assertEquals(Collections.emptyList(), debtList);
    }

    @Test
    public void getAllDebtsByUserId_whenUserExisting_returnDebts() {
        when(userService.getUserById(anyLong())).thenReturn(Optional.of(person));

        Debt debt = new Debt();
        debt.setId(1L);
        debt.setTitle("Test debt");
        debt.setPayer(person);

        when(debtRepository.findAllByPersonParticipating(person)).thenReturn(Collections.singletonList(debt));
        assertEquals(1, debtService.getAllDebtsByUserId(3L).size());
    }

    @Test
    public void getTotalBalanceForUser_shouldAddAllDebtsTogether() {
        Debt debt1 = new Debt();
        debt1.setSum(BigDecimal.valueOf(100));
        debt1.setPayer(Person.builder().id(1L).build());
        Debt debt2 = new Debt();
        debt2.setSum(BigDecimal.valueOf(100));
        debt2.setPayer(Person.builder().id(2L).build());

        List<Debt> debts = Arrays.asList(debt1, debt2);

        doReturn(debts).when(debtService).getAllDebtsByUserId(1L);

        BigDecimal total = debtService.getTotalDebtBalanceForUser(1L);
        assertEquals(BigDecimal.ZERO, total);
    }
}