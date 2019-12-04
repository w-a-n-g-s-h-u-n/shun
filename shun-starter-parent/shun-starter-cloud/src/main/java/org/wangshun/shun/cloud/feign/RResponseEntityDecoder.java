package org.wangshun.shun.cloud.feign;

import java.io.IOException;
import java.lang.reflect.Type;

import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.wangshun.shun.core.exception.ServiceException;
import org.wangshun.shun.core.http.R;

import feign.FeignException;
import feign.Response;
import feign.codec.DecodeException;
import feign.codec.Decoder;

public class RResponseEntityDecoder extends ResponseEntityDecoder {

    public RResponseEntityDecoder(Decoder decoder) {
        super(decoder);
    }

    @Override
    public Object decode(Response response, Type type) throws IOException, DecodeException, FeignException {
        Object decodedObject = super.decode(response, type);
        if (decodedObject instanceof R) {
            R r = (R)decodedObject;
            if (null == r.getCode() || !r.getCode().equals(0)) {
                throw new ServiceException(r.getMsg());
            }
        }
        return decodedObject;
    }
}
