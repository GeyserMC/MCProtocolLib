package org.geysermc.mcprotocollib.protocol.data.game.item.component;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * Wrapper around a map of data components and their respective values.
 *
 * <p>This map can either be a complete data component map, or a data component patch to another map. The meaning of {@code null} values in the map depends on if the map
 * is a patch or full map. If the map:</p>
 *
 * <ul>
 *     <li>Is a full map, {@code null} means an absence of the component in the map. {@link DataComponents#put(DataComponentType, Object)} should not be used with {@code null} values, rather {@link DataComponents#remove(DataComponentType)} should be used.</li>
 *     <li>Is a patch, {@code null} can mean an absence of the component in the patch, or that the component should be removed from a map the patch is applied to. Use {@link DataComponents#contains(DataComponentType)}, which returns {@code true} for the latter, to check which is the case.</li>
 * </ul>
 */
@Data
@AllArgsConstructor
public class DataComponents {
    private final Map<DataComponentType<?>, DataComponent<?, ?>> dataComponents;

    @Nullable
    @SuppressWarnings("unchecked")
    public <T> T get(DataComponentType<T> type) {
        DataComponent<?, ?> component = dataComponents.get(type);
        return component == null ? null : (T) component.getValue();
    }

    public <T> T getOrDefault(DataComponentType<T> type, T def) {
        T value = get(type);
        return value != null ? value : def;
    }

    /**
     * @param value should only be {@code null} if this map is a patch to another map and specifying the removal of the specific component.
     */
    public <T> void put(DataComponentType<T> type, @Nullable T value) {
        if (value == null) {
            dataComponents.put(type, type.readNullDataComponent());
        } else if (type instanceof IntComponentType intType) {
            dataComponents.put(intType, intType.primitiveFactory.createPrimitive(intType, (Integer) value));
        } else if (type instanceof BooleanComponentType boolType) {
            dataComponents.put(boolType, boolType.primitiveFactory.createPrimitive(boolType, (Boolean) value));
        } else {
            dataComponents.put(type, type.dataComponentFactory.create(type, value));
        }
    }

    public boolean contains(DataComponentType<?> component) {
        return dataComponents.containsKey(component);
    }

    @SuppressWarnings("unchecked")
    public <T> @Nullable T remove(DataComponentType<T> component) {
        DataComponent<T, ?> removed = (DataComponent<T, ?>) dataComponents.remove(component);
        return removed == null ? null : removed.getValue();
    }

    public DataComponents clone() {
        return new DataComponents(new HashMap<>(dataComponents));
    }
}
