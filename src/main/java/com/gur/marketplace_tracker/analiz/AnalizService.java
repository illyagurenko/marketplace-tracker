package com.gur.marketplace_tracker.analiz;

import com.gur.marketplace_tracker.strategy.LamodaStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnalizService {
    private final LamodaStrategy lamodaStrategy;

    @Autowired
    public AnalizService(LamodaStrategy lamodaStrategy) {
        this.lamodaStrategy = lamodaStrategy;
    }
}
