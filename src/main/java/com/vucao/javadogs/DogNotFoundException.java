package com.vucao.javadogs;

public class DogNotFoundException extends RuntimeException
{
    public DogNotFoundException(Long id)
    {
        super("Coud not find dog " + id);
    }
}

