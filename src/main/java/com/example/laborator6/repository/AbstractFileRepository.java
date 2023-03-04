package com.example.laborator6.repository;

import com.example.laborator6.domain.Entity;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public abstract class AbstractFileRepository<ID, E extends Entity<ID>> extends InMemoryRepository<ID, E> {

    private final String fileName;

    public AbstractFileRepository(String fileName) {
        this.fileName = fileName;
        loadFromFile();
    }

    @Override
    public E save(E entity) {
        E e = super.save(entity);
        if (e == null) {
            appendToFile(entity);
        }
        return e;
    }

    @Override
    public E delete(ID idEntity) {
        E e = super.delete(idEntity);
        if (e != null) {
            writeToFile();
        }
        return e;
    }

    @Override
    public E update(E entity) {
        E e = super.update(entity);
        if (e == null) {
            writeToFile();
        }
        return e;
    }

    @Override
    public E findOne(ID idEntity) {
        return super.findOne(idEntity);
    }

    @Override
    public Iterable<E> findAll() {
        return super.findAll();
    }

    @Override
    public Map<ID, E> getEntities() {
        return super.getEntities();
    }

    public abstract E extractEntity(List<String> attributes);

    public abstract String createEntityAsString(E entity);

    private void loadFromFile() {

        try (BufferedReader buffer = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = buffer.readLine()) != null) {
                if (line.length() > 0) {
                    List<String> attributes = Arrays.asList(line.split(";"));
                    if (attributes.size() == 3) {
                        E e = extractEntity(attributes);
                        super.save(e);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void appendToFile(E entity) {
        try (BufferedWriter buffer = new BufferedWriter(new FileWriter(fileName, true))) {
            buffer.write(createEntityAsString(entity));
            buffer.newLine();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    private void writeToFile() {
        try (BufferedWriter buffer = new BufferedWriter(new FileWriter(fileName))) {
            for (E entity : super.findAll()) {
                buffer.write(createEntityAsString(entity));
                buffer.newLine();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

}
