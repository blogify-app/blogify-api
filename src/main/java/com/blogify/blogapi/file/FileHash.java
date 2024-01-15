package com.blogify.blogapi.file;

import com.blogify.blogapi.PojaGenerated;

@PojaGenerated
public record FileHash(FileHashAlgorithm algorithm, String value) {}
