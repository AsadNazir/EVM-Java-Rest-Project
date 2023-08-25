package com.example.evm_2.commons;

public class Response {
    boolean error;
    Object data;

    @Override
    public String toString() {
        return "Response{" +
                "error=" + error +
                ", data=" + data +
                '}';
    }

    public Response() {
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

    public Response(boolean error, Object data) {
        this.error = error;
        this.data = data;
    }
}
