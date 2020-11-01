package com.rent.Base;

import org.springframework.stereotype.Component;

@Component
public interface TokenGenerator {
    public String generate(String[] strings);
}
