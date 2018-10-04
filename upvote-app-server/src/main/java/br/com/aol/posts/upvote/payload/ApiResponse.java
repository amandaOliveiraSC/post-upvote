package br.com.aol.posts.upvote.payload;

public class ApiResponse {

	private Boolean success;

	private String message;

	public ApiResponse(final Boolean success, final String message) {
		this.success = success;
		this.message = message;
	}

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(final Boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(final String message) {
		this.message = message;
	}
}
