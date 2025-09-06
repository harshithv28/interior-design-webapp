package com.interior.design.dto;

import com.interior.design.domain.Media;

public class MediaDtos {
    public record UploadMediaRequest(Media.Type type, String url) {}
}

