package com.epam.training.ticketservice.backend.pricecomponent.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class PriceComponentDto {

    private final String name;
    private final Integer value;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String name;
        private Integer value;

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withValue(Integer value) {
            this.value = value;
            return this;
        }

        public PriceComponentDto build() {
            return new PriceComponentDto(name, value);
        }

    }

}
