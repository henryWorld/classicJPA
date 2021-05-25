package com.specsavers.socrates.clinical.reader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

@Component
public class ResourceReader {

	private final ResourceLoader resourceLoader;

	@Autowired
	public ResourceReader(ResourceLoader resourceLoader) {
		super();

		this.resourceLoader = resourceLoader;
	}

	public String asString(String resourceLocation, Charset charset) {
		Resource resource = this.resourceLoader.getResource(resourceLocation);

		try (Reader reader = new InputStreamReader(resource.getInputStream(), charset)) {
			return FileCopyUtils.copyToString(reader);
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

}
