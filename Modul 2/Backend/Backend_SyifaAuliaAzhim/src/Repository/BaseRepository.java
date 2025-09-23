package Repository;

import java.util.*;

public abstract class BaseRepository<T, ID> {
    Map<ID, T> dataMap = new HashMap<>();
    protected List<T> allData = new ArrayList<>();

    public Optional<T> findById(ID id) {
        return Optional.ofNullable(dataMap.get(id));
    }

    public List<T> findAll() {
        return new ArrayList<>(allData);
    }

    public abstract void save(T entity);
    public abstract ID getId(T entity);
}