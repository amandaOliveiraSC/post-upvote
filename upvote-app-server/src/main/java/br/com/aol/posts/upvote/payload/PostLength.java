package br.com.aol.posts.upvote.payload;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

public class PostLength {

	@NotNull
    @Max(7)
    private Integer days;

    @NotNull
    @Max(23)
    private Integer hours;

    public int getDays() {
        return days;
    }

    public void setDays(final int days) {
        this.days = days;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(final int hours) {
        this.hours = hours;
    }
}
