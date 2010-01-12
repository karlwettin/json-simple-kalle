package org.json.simple.serialization;

import sun.reflect.generics.reflectiveObjects.TypeVariableImpl;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

import org.json.simple.serialization.primitives.*;
import org.json.simple.serialization.collections.CollectionCodec;
import org.json.simple.serialization.collections.ArrayCodec;
import org.json.simple.serialization.collections.MapCodec;

/**
 * For performance reasons you should only have one instance of this class available.
 * Make it singleton or what not.
 *
 * @author kalle@apache.org
 * @since 2009-jul-29 21:18:08
 */
public class CodecRegistry {

  public CodecRegistry() {
    primitiveCodecs.put(boolean.class, new BooleanCodec());
    primitiveCodecs.put(byte.class, new ByteCodec());
    primitiveCodecs.put(short.class, new ShortCodec());
    primitiveCodecs.put(int.class, new IntegerCodec());
    primitiveCodecs.put(long.class, new LongCodec());
    primitiveCodecs.put(float.class, new FloatCodec());
    primitiveCodecs.put(double.class, new DoubleCodec());
    primitiveCodecs.put(char.class, new CharacterCodec());

    primitiveCodecs.put(java.lang.Boolean.class, new BooleanCodec());
    primitiveCodecs.put(java.lang.Byte.class, new ByteCodec());
    primitiveCodecs.put(java.lang.Short.class, new ShortCodec());
    primitiveCodecs.put(java.lang.Integer.class, new IntegerCodec());
    primitiveCodecs.put(java.lang.Long.class, new LongCodec());

    primitiveCodecs.put(java.lang.Float.class, new FloatCodec());
    primitiveCodecs.put(java.lang.Double.class, new DoubleCodec());

    primitiveCodecs.put(java.math.BigInteger.class, new BigIntegerCodec());
    primitiveCodecs.put(java.math.BigDecimal.class, new BigDecimalCodec());

    primitiveCodecs.put(java.lang.Character.class, new CharacterCodec());
    primitiveCodecs.put(java.lang.String.class, new StringCodec());

    primitiveCodecs.put(java.util.Date.class, new DateCodec());
  }

  private Map<Class, Codec> primitiveCodecs = new HashMap<Class, Codec>();

  private Map<Class, BeanCodec> beanCodecs = new HashMap<Class, BeanCodec>();

  public <T> Codec<T> getCodec(Class<T> type) {
    Codec codec = primitiveCodecs.get(type);
    if (codec != null) {
      return codec;
    }
    codec = beanCodecs.get(type);
    if (codec != null) {
      return codec;
    }

    BeanCodec beanCodec = new BeanCodec();
    beanCodecs.put(type, beanCodec);
    // codec must be in map at this point to avoid eternal loop.
    beanCodec.resolve(this, type);
    return beanCodec;

  }

  /**
   *
   * @param field
   * @param instance if not null the classtype of this instance will be choosed over the class type of the field.  
   * @return
   */
  public Codec getCodec(Field field, Object instance) {
    if (field.getType().isArray()) {
      Codec codec = getCodec(field.getType());
      if (codec == null) {
        codec = new ArrayCodec(getPrimitiveGenericType(field));
      }
      return codec;

    } else if (List.class.isAssignableFrom(field.getType())) {
      Class genericType = getPrimitiveGenericType(field);
      return new CollectionCodec(this, genericType) {
        @Override
        public Collection collectionFactory() {
          return new ArrayList();
        }
      };

    } else if (Set.class.isAssignableFrom(field.getType())) {
      Class genericType = getPrimitiveGenericType(field);

      return new CollectionCodec(this, genericType) {
        @Override
        public Collection collectionFactory() {
          return new LinkedHashSet();
        }
      };

    } else if (Map.class.isAssignableFrom(field.getType())) {
      Class[] genericTypes = getGenericTypes(field);
      return new MapCodec(genericTypes[0], genericTypes[1]);
    }

    if (instance == null) {
      return getCodec(field.getType());
    } else {
      return getCodec(instance.getClass());
    }
  }


  /**
   * @param field
   * @return class of values in a collection, e.g. Integer in List<Integer>.
   */
  private Class getPrimitiveGenericType(Field field) {
    Type type = field.getGenericType();
    if (type instanceof ParameterizedType) {
      ParameterizedType pt = (ParameterizedType) type;
      if (pt.getActualTypeArguments()[0] instanceof TypeVariableImpl) {
        // so this is a List<D> where D is defined in the owner class.
        // this value is not read here,
        // instead the class of the type is picked up from the json value "class" (Codec#classIdentifier) of each instance.
        return null;
      } else {
        return (Class) pt.getActualTypeArguments()[0];
      }
    } else {
      throw new RuntimeException("List is missing generic type argument! " + field.toString());
    }
  }

  private Class[] getGenericTypes(Field field) {
    Type type = field.getGenericType();
    if (type instanceof ParameterizedType) {
      ParameterizedType pt = (ParameterizedType) type;
      Class[] result = new Class[pt.getActualTypeArguments().length];
      for (int i = 0; i < pt.getActualTypeArguments().length; i++) {
        result[i] = (Class) pt.getActualTypeArguments()[i];
      }
      return result;
    } else {
      throw new RuntimeException("List is missing generic type argument!");
    }
  }

  public Map<Class, Codec> getPrimitiveCodecs() {
    return primitiveCodecs;
  }

  public Map<Class, BeanCodec> getBeanCodecs() {
    return beanCodecs;
  }


}
