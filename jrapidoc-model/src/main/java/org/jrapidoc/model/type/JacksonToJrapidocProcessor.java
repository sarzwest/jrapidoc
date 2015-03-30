package org.jrapidoc.model.type;

import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.std.*;
import com.fasterxml.jackson.databind.type.*;
import org.jrapidoc.model.object.type.*;
import org.jrapidoc.model.object.BeanProperty;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by papa on 11.1.15.
 */
public class JacksonToJrapidocProcessor {

    ObjectMapper objectMapper;
    public static Map<String, Type> cache = new HashMap<String, Type>();

    public JacksonToJrapidocProcessor(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Z jackson typu vytvori apition typ, bean properties jsou ulozeny v cache
     * @param jacksonType
     * @return
     */
    public Type getType(SimpleType jacksonType) {
        try {
            String signature = JacksonSignature.createSignature(jacksonType);
            CustomType type = new CustomType(jacksonType.getRawClass().getName(), signature, jacksonType.getRawClass());
            if (cache.containsKey(signature)) {
                return cache.get(signature);
            }
            cache.put(signature, type);
            ObjectWriter objectWriter = objectMapper.writerFor(jacksonType);
            Field prefetchFiled = objectWriter.getClass().getDeclaredField("_prefetch");
            prefetchFiled.setAccessible(true);
            ObjectWriter.Prefetch prefetch = (ObjectWriter.Prefetch) prefetchFiled.get(objectWriter);
            doIntrospection(prefetch.valueSerializer, type);
            return type;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * To stejne jako {@link #getType(com.fasterxml.jackson.databind.type.SimpleType)}
     * @param jacksonType
     * @return
     */
    public Type getType(CollectionLikeType jacksonType) {
        try {
            String signature = JacksonSignature.createSignature(jacksonType);
            JavaType contentType = jacksonType.getContentType();
            String contentSignature = JacksonSignature.createSignature(contentType);
            Class<?> containerClass = jacksonType.getRawClass();
            CollectionTypeJrapidoc type = new CollectionTypeJrapidoc(containerClass.getName(), signature, contentType.getRawClass().getName(), contentSignature);
            if (cache.containsKey(signature)) {
                return cache.get(signature);
            }
            cache.put(signature, type);
            ObjectWriter objectWriter = objectMapper.writerFor(jacksonType);
            Field prefetchFiled = objectWriter.getClass().getDeclaredField("_prefetch");
            prefetchFiled.setAccessible(true);
            ObjectWriter.Prefetch prefetch = (ObjectWriter.Prefetch) prefetchFiled.get(objectWriter);
            doIntrospection(prefetch.valueSerializer, type);
            return type;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * To stejne jako {@link #getType(com.fasterxml.jackson.databind.type.SimpleType)}
     * @param jacksonType
     * @return
     */
    public Type getType(ArrayType jacksonType) {
        try {
            String signature = JacksonSignature.createSignature(jacksonType);
            String contentSignature = JacksonSignature.createSignature(jacksonType.getContentType());
            Class<?> contentType = jacksonType.getContentType().getRawClass();
            Class<?> containerClass = jacksonType.getRawClass();
            CollectionTypeJrapidoc type = new CollectionTypeJrapidoc(containerClass.getName(), signature, contentType.getName(), contentSignature);
            if (cache.containsKey(signature)) {
                return cache.get(signature);
            }
            cache.put(signature, type);
            ObjectWriter objectWriter = objectMapper.writerFor(jacksonType);
            Field prefetchFiled = objectWriter.getClass().getDeclaredField("_prefetch");
            prefetchFiled.setAccessible(true);
            ObjectWriter.Prefetch prefetch = (ObjectWriter.Prefetch) prefetchFiled.get(objectWriter);
            doIntrospection(prefetch.valueSerializer, type);
            return type;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * To stejne jako {@link #getType(com.fasterxml.jackson.databind.type.SimpleType)}
     * @param jacksonType
     * @return
     */
    public Type getType(MapLikeType jacksonType) {
        try {
            String signature = JacksonSignature.createSignature(jacksonType);
            JavaType keyType = jacksonType.getKeyType();
            JavaType valueType = jacksonType.getContentType();
            Class<?> containerClass = jacksonType.getRawClass();
            MapTypeJrapidoc type = new MapTypeJrapidoc(containerClass.getName(), signature, keyType.getRawClass().getName(), keyType.toString(), valueType.getRawClass().getName(), valueType.toString());
            if (cache.containsKey(signature)) {
                return cache.get(signature);
            }
            cache.put(signature, type);
            ObjectWriter objectWriter = objectMapper.writerFor(jacksonType);
            Field prefetchFiled = objectWriter.getClass().getDeclaredField("_prefetch");
            prefetchFiled.setAccessible(true);
            ObjectWriter.Prefetch prefetch = (ObjectWriter.Prefetch) prefetchFiled.get(objectWriter);
            doIntrospection(prefetch.valueSerializer, type);
            return type;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Provadi presmerovani z obecneho typu na konkretni typ
     * @param type jackson typ
     * @return apition typ
     */
    public Type getType(JavaType type) {
        if (type instanceof SimpleType) {
            return getType((SimpleType) type);
        } else if (type instanceof CollectionType) {
            return getType((CollectionLikeType) type);
        }else if(type instanceof ArrayType){
            return getType((ArrayType)type);
        }else if(type instanceof MapLikeType){
            return getType((MapLikeType)type);
        }
        throw new RuntimeException("Sem se to nesmi dostat: " + type);
    }

    /**
     * Provadi presmerovani z obecneho jackson serializeru na konkretnejsi
     * @param serializer
     * @param type
     */
    private void doIntrospection(JsonSerializer serializer, Type type) {
        if(serializer == null){
            //Object.class has no serializer
            return;
        }
        if (EnumSerializer.class.isAssignableFrom(serializer.getClass())) {
            introspectSerializer((EnumSerializer) serializer, (CustomType) type);
        }else if (BeanSerializerBase.class.isAssignableFrom(serializer.getClass())) {
            introspectSerializer((BeanSerializerBase) serializer, (CustomType) type);
        } else if (StdScalarSerializer.class.isAssignableFrom(serializer.getClass())) {
            introspectSerializer((StdScalarSerializer) serializer, (CustomType) type);
        } else if (AsArraySerializerBase.class.isAssignableFrom(serializer.getClass())) {
            introspectSerializer((AsArraySerializerBase) serializer, (CollectionTypeJrapidoc) type);
        }else if(MapSerializer.class.isAssignableFrom(serializer.getClass())){
            introspectSerializer((MapSerializer)serializer, (MapTypeJrapidoc)type);
        }
    }

    /**
     * Prozkoumava serializer pro kolekce
     * @param collectionSerializer
     * @param type
     */
    private void introspectSerializer(AsArraySerializerBase collectionSerializer, CollectionTypeJrapidoc type) {
        getType(collectionSerializer.getContentType());
    }

    /**
     * Prozkoumava serializer pro javovske beany
     * @param beanSerializer
     * @param type
     */
    private void introspectSerializer(BeanSerializerBase beanSerializer, CustomType type) {
        try {
            Field propsField = beanSerializer.getClass().getSuperclass().getDeclaredField("_props");
            propsField.setAccessible(true);
            BeanPropertyWriter[] props = (BeanPropertyWriter[]) propsField.get(beanSerializer);
            for (BeanPropertyWriter prop : props) {
                JavaType propType = prop.getType();
                getType(propType);
                String signature = JacksonSignature.createSignature(propType);
                type.addBeanProperty(new BeanProperty(prop.getName(), signature, prop.getPropertyType()));
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Prozkoumava serializer pro enumerace
     * @param enumSerializer
     * @param type
     */
    private void introspectSerializer(EnumSerializer enumSerializer, CustomType type) {
        for (SerializableString value : enumSerializer.getEnumValues().values()) {
            type.addEnumeration(value.getValue());
        }
    }

    /**
     * Prozkoumava serializer pro mapu
     * @param mapSerializer
     * @param type
     */
    private void introspectSerializer(MapSerializer mapSerializer, MapTypeJrapidoc type) {
        try {
            Field keyTypeField = mapSerializer.getClass().getDeclaredField("_keyType");
            keyTypeField.setAccessible(true);
            JavaType keyType = (JavaType)keyTypeField.get(mapSerializer);
            JavaType valueType = mapSerializer.getContentType();
            getType(keyType);
            getType(valueType);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Prozkoumava standardni javovske typy - ty se nemuseji zkoumat, takze nic nedela
     * @param stdScalarSerializer
     * @param type
     */
    private void introspectSerializer(StdScalarSerializer stdScalarSerializer, CustomType type) {
    }

    public Type loadType(JavaType javaType){
        Type type = getType(javaType);
        return type;
    }
}
