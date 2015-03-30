package org.jrapidoc.model.type;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jrapidoc.model.object.type.Type;
import sun.reflect.generics.reflectiveObjects.TypeVariableImpl;

import java.lang.reflect.ParameterizedType;

/**
 * Created by papa on 25.3.15.
 */
public class JacksonTypeProvider extends TypeProvider {

    protected ObjectMapper objectMapper;
    protected JacksonToJrapidocProcessor processor;

    public JacksonTypeProvider() {
        objectMapper = new ObjectMapper();
        processor = new JacksonToJrapidocProcessor(objectMapper);
    }

    @Override
    public Type createType(java.lang.reflect.Type genericType) {
        JavaType javaType = createJavaType(genericType);
        Type loadType = processor.loadType(javaType);
        return loadType;
    }

    private JavaType createJavaType(java.lang.reflect.Type genericType) {
        JavaType javaType = null;
        if(genericType instanceof Class<?>){
//            jedna se o custom type
            javaType = objectMapper.getTypeFactory().constructParametrizedType((Class)genericType, (Class)genericType, new Class[]{});
        }else if(genericType instanceof ParameterizedType){
            //jedna se o parametrizovany typ
            ParameterizedType paramType = (ParameterizedType)genericType;
            java.lang.reflect.Type[] genericTypes = paramType.getActualTypeArguments();
            //Kdyz je typ genericky napr <T>, nahrad ho za Object
            for(int i = 0;i < genericTypes.length;i++){
                if(genericTypes[i] instanceof TypeVariableImpl){
                    genericTypes[i] = Object.class;
                }
            }
            JavaType[] javaTypes = new JavaType[genericTypes.length];
            for (int i = 0;i < javaTypes.length;i++){
                javaTypes[i] = createJavaType(genericTypes[i]);
            }
            //prochazej generictypes a vytvarej z kazdeho JavaType a davej do pole
//            Class[] typeArguments = Arrays.copyOf(genericTypes, genericTypes.length, Class[].class);
//          bad  javaType = JacksonUtil.objectMapperInstance().getTypeFactory().constructParametrizedType(genericType, genericType, typeArguments);
            javaType = objectMapper.getTypeFactory().constructParametrizedType((Class)paramType.getRawType(), (Class)paramType.getRawType(), javaTypes);
        }else if(genericType instanceof TypeVariableImpl){
            javaType = objectMapper.getTypeFactory().constructParametrizedType(Object.class, Object.class, new Class<?>[]{});
        }
        return javaType;
    }
}
