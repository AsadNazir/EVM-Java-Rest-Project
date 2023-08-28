package com.example.evm_2.commons;

public class CustomResponse {
    boolean error;
    Object data;

    @Override
    public String toString() {
        return "Response{" +
                "error=" + error +
                ", data=" + data +
                '}';
    }

    public CustomResponse() {
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public CustomResponse(boolean error, Object data) {
        this.error = error;
        this.data = data;
    }
}
