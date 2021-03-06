package ee.taltech.debty.service;

import ee.taltech.debty.entity.Bill;
import ee.taltech.debty.repository.BillPaymentRepository;
import ee.taltech.debty.repository.BillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BillService {

    private final BillRepository billRepository;
    private final BillPaymentRepository billPaymentRepository;

    public Optional<Bill> getBillById(Long id) {
        return billRepository.findById(id);
    }

    Bill saveBill(Bill bill) {
        bill.getBillPayments().forEach(billPayment -> billPayment.setModifiedAt(LocalDateTime.now()));
        billPaymentRepository.saveAll(bill.getBillPayments());
        bill.setModifiedAt(LocalDateTime.now());
        return billRepository.save(bill);
    }

    void deleteBillById(Long id) {
        billRepository.findById(id).ifPresent(billRepository::delete);
    }
}
