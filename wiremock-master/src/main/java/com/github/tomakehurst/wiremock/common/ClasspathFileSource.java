/*
 * Copyright (C) 2011 Thomas Akehurst
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.tomakehurst.wiremock.common;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Iterators;
import com.google.common.io.Resources;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static com.github.tomakehurst.wiremock.common.Exceptions.throwUnchecked;
import static com.google.common.base.MoreObjects.firstNonNull;
import static com.google.common.collect.Iterables.transform;
import static com.google.common.collect.Iterators.find;
import static com.google.common.collect.Iterators.forEnumeration;
import static com.google.common.collect.Lists.newArrayList;
import static java.lang.Thread.currentThread;
import static java.util.Arrays.asList;

public class ClasspathFileSource implements FileSource {

    private final String path;
    private ZipFile zipFile;
    private File rootDirectory;

    public ClasspathFileSource(String path) {
        this.path = path;

        try {
            URL resource = firstNonNull(
                    currentThread().getContextClassLoader(),
                    Resources.class.getClassLoader())
                        .getResource(path);
            if (resource == null) {
                rootDirectory = new File(path);
                return;
            }

            URI pathUri = resource.toURI();

            if (asList("jar", "war", "ear", "zip").contains(pathUri.getScheme())) {
                String jarFileUri = pathUri.getSchemeSpecificPart().split("!")[0];
                File file = new File(URI.create(jarFileUri));
                zipFile = new ZipFile(file);
            } else if (pathUri.getScheme().equals("file")) {
                rootDirectory = new File(pathUri);
            } else {
                throw new RuntimeException("ClasspathFileSource can't handle paths of type " + pathUri.getScheme());
            }

        } catch (Exception e) {
            throwUnchecked(e);
        }
    }

    private boolean isFileSystem() {
        return rootDirectory != null;
    }

    @Override
    public BinaryFile getBinaryFileNamed(final String name) {
        if (isFileSystem()) {
            return new BinaryFile(new File(rootDirectory, name).toURI());
        }

        ZipEntry zipEntry = find(forEnumeration(zipFile.entries()), new Predicate<ZipEntry>() {
            public boolean apply(ZipEntry input) {
                return input.getName().equals(path + "/" + name);
            }
        });

        return new BinaryFile(getUriFor(zipEntry));
    }

    @Override
    public void createIfNecessary() {
        throw new UnsupportedOperationException("Classpath file sources are read-only");
    }

    @Override
    public FileSource child(String subDirectoryName) {
        return new ClasspathFileSource(path + "/" + subDirectoryName);
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public List<TextFile> listFilesRecursively() {
        if (isFileSystem()) {
            assertExistsAndIsDirectory();
            List<File> fileList = newArrayList();
            recursivelyAddFilesToList(rootDirectory, fileList);
            return toTextFileList(fileList);
        }

        return FluentIterable.from(toIterable(zipFile.entries())).filter(new Predicate<ZipEntry>() {
            public boolean apply(ZipEntry jarEntry) {
                return !jarEntry.isDirectory() && jarEntry.getName().startsWith(path);
            }
        }).transform(new Function<ZipEntry, TextFile>() {
            public TextFile apply(ZipEntry jarEntry) {
                return new TextFile(getUriFor(jarEntry));
            }
        }).toList();
    }

    private URI getUriFor(ZipEntry jarEntry) {
        try {
            return Resources.getResource(jarEntry.getName()).toURI();
        } catch (URISyntaxException e) {
            return throwUnchecked(e, URI.class);
        }
    }

    private void recursivelyAddFilesToList(File root, List<File> fileList) {
        File[] files = root.listFiles();
        for (File file: files) {
            if (file.isDirectory()) {
                recursivelyAddFilesToList(file, fileList);
            } else {
                fileList.add(file);
            }
        }
    }

    private List<TextFile> toTextFileList(List<File> fileList) {
        return newArrayList(transform(fileList, new Function<File, TextFile>() {
            public TextFile apply(File input) {
                return new TextFile(input.toURI());
            }
        }));
    }

    @Override
    public void writeTextFile(String name, String contents) {
        throw new UnsupportedOperationException("Classpath file sources are read-only");
    }

    @Override
    public void writeBinaryFile(String name, byte[] contents) {
        throw new UnsupportedOperationException("Classpath file sources are read-only");
    }

    @Override
    public boolean exists() {
        // It'll only be non-file system if finding the classpath resource succeeded in the constructor
        return (isFileSystem() && rootDirectory.exists()) || (!isFileSystem());
    }

    private static <T> Iterable<T> toIterable(final Enumeration<T> e) {
        return new Iterable<T>() {
            @Override
            public Iterator<T> iterator() {
                return Iterators.forEnumeration(e);
            }
        };
    }

    private void assertExistsAndIsDirectory() {
        if (rootDirectory.exists() && !rootDirectory.isDirectory()) {
            throw new RuntimeException(rootDirectory + " is not a directory");
        } else if (!rootDirectory.exists()) {
            throw new RuntimeException(rootDirectory + " does not exist");
        }
    }
}
