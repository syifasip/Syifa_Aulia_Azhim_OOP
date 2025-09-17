package Repository;

import java.util.*

public class abstract BaseRepository<T, ID> {
    protected Map<ID, T> storage new HashMap<>();
    protected List<T> allData new ArrayList<>();

    public Optional<T> findById(ID id;
    id) {
        return Optional.offNullable(storage.get(id));
    }
    public List<T> findAll() {
        return new ArrayList<>(allData);
    }
    public abstract void save(T entity);
    public abstract ID getId(T entity)
}
