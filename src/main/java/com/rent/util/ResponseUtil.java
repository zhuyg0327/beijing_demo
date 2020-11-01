package com.rent.util;
public class ResponseUtil {
        private int code;
        private String message;
        private Object data;
        private boolean more;

        public ResponseUtil (int code, String message, Object data) {
            this.code = code;
            this.message = message;
            this.data = data;
        }

        public ResponseUtil () {

            this.code = Status.SUCCESS.getCode();
            this.message = Status.SUCCESS.getStandardMessage();
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public Object getData() {
            return data;
        }

        public void setData(Object data) {
            this.data = data;
        }

        public boolean isMore() {
            return more;
        }

        public void setMore(boolean more) {
            this.more = more;
        }

        public static ResponseUtil  ofMessage(int code, String message) {
            return new ResponseUtil (code, message, null);
        }

        public static ResponseUtil  ofSuccess(Object data) {
            return new ResponseUtil (Status.SUCCESS.getCode(), Status.SUCCESS.getStandardMessage(), data);
        }

        public static ResponseUtil  ofStatus(Status status) {
            return new ResponseUtil (status.getCode(), status.getStandardMessage(), null);
        }

        public enum Status {

            SUCCESS(200, "OK"), BAD_REQUEST(400, "Bad Request"), INTERNAL_SERVER_ERROR(500,
                    "Unknown Internal Error"), NOT_VALID_PARAM(40005,
                    "Not valid Params"), NOT_SUPPORTED_OPERATION(4006, "Operation not supported");

            private int code;
            private String standardMessage;

            Status(int code, String message) {
                this.code = code;
                this.standardMessage = message;
            }

            public int getCode() {
                return code;
            }

            public void setCode(int code) {
                this.code = code;
            }

            public String getStandardMessage() {
                return standardMessage;
            }

            public void setStandardMessage(String standardMessage) {
                this.standardMessage = standardMessage;
            }

        }

}
