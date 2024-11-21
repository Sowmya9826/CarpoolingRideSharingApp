package com.carpoolapp.carpoolService.models.enums;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class ErrorCodeTest {

    @Test
    public void testErrorCodeEnumValues() {
        // Verify that the enum values are correctly set
        assertThat(ErrorCode.UNSPECIFIED).isNotNull();
        assertThat(ErrorCode.NO_RIDE_AVAILABLE).isNotNull();
    }

    @Test
    public void testErrorCodeMessages() {
        // Verify that the error messages for each enum value are correct
        assertThat(ErrorCode.UNSPECIFIED.getErrorMessage()).isEqualTo("Sorry, Something Went Wrong!");
        assertThat(ErrorCode.NO_RIDE_AVAILABLE.getErrorMessage()).isEqualTo("Sorry, Right now there are not riders available!");
    }

    @Test
    public void testEnumName() {
        // Verify the name of the enum constants
        assertThat(ErrorCode.UNSPECIFIED.name()).isEqualTo("UNSPECIFIED");
        assertThat(ErrorCode.NO_RIDE_AVAILABLE.name()).isEqualTo("NO_RIDE_AVAILABLE");
    }
}
