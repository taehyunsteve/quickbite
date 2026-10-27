package quickbite.boundary.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import quickbite.controller.dto.CheckoutCommand;

public record CheckoutRequest(
        @NotBlank String idPelanggan,
        @NotBlank String tokenKartu,
        @NotEmpty List<@Valid CartItem> keranjang) {

    public record CartItem(
            @NotBlank String idMenu,
            @Min(1) int qty) { }

    public CheckoutCommand toCommand() {
        return new CheckoutCommand(
                idPelanggan,
                tokenKartu,
                keranjang.stream()
                        .map(i -> new CheckoutCommand.Item(i.idMenu(), i.qty()))
                        .toList());
    }
}
