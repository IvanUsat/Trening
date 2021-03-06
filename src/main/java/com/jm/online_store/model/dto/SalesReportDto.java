package com.jm.online_store.model.dto;

import com.jm.online_store.model.Order;
import com.jm.online_store.model.User;
import com.opencsv.bean.CsvBindByPosition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SalesReportDto {
    @CsvBindByPosition(position = 0)
    private Long orderNumber;
    @CsvBindByPosition(position = 2)
    private String userEmail;
    @CsvBindByPosition(position = 3)
    private String customerInitials;
    @CsvBindByPosition(position = 1)
    private LocalDate purchaseDate;
    @CsvBindByPosition(position = 5)
    private Long quantity;
    @CsvBindByPosition(position = 4)
    private String listOfProducts;
    @CsvBindByPosition(position = 6)
    private Double orderSummaryPrice;

    /**
     * Static method which converts {@link Order} into SalesReportDto
     * @param order - {@link Order}
     * @return - {@link SalesReportDto}
     */
    public static SalesReportDto orderToSalesReportDto(Order order) {
        SalesReportDto salesReportDto = new SalesReportDto();
        salesReportDto.setOrderNumber(order.getId());
        salesReportDto.setUserEmail(order.getUser().getEmail());
        salesReportDto.setCustomerInitials(createUserInitialsFromUser(order.getUser()));
        salesReportDto.setPurchaseDate(order.getDateTime().toLocalDate());
        salesReportDto.setQuantity(order.getAmount());
        salesReportDto.setListOfProducts(order
                .getProductInOrders()
                .stream()
                .map(product -> String.valueOf(product.getProduct().getProduct() + " - (" + product.getAmount() + ")"))
                .collect(Collectors.joining(", ")));
        salesReportDto.setOrderSummaryPrice(order.getOrderPrice());
        return salesReportDto;
    }

    /**
     * Static method that creates String with customer initials for dto
     *
     * @param user - {@link User} which made purchase
     * @return - String with initials if user have First and Last name or return "????????????????????"
     */
    private static String createUserInitialsFromUser(User user) {
        StringBuilder userInitials = new StringBuilder();
        if (user.getFirstName() != null && !user.getFirstName().equals("")) {
            userInitials.append(user.getFirstName());
            if (user.getLastName() != null && !user.getLastName().equals("")) {
                userInitials.append(" ");
                userInitials.append(user.getLastName());
                return userInitials.toString();
            }
            return userInitials.toString();
        }
        return "????????????????????";
    }
}
