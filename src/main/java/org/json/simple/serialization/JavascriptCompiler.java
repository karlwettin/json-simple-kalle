package org.json.simple.serialization;

import org.json.simple.serialization.collections.ArrayCodec;
import org.json.simple.serialization.collections.CollectionCodec;
import org.json.simple.serialization.collections.MapCodec;
import org.json.simple.serialization.collections.PrimitiveArrayCodec;
import org.json.simple.serialization.primitives.CharacterCodec;
import org.json.simple.serialization.primitives.DateCodec;
import org.json.simple.serialization.primitives.StringCodec;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * @author karl.wettin@kodapan.se
 * @since 2011-02-03 22.31
 */
public class JavascriptCompiler {

  private CodecRegistry registry;
  private PrintWriter js;

  public JavascriptCompiler(CodecRegistry registry, PrintWriter js) {
    this.registry = registry;
    this.js = js;
  }

  public void compile() throws IllegalAccessException, InstantiationException {

    Date now = new Date();

    Map<Class, String> compiled = new HashMap<Class, String>();

    for (BeanCodec codec : new ArrayList<BeanCodec>(registry.getBeanCodecs().values())) {

      StringWriter sw = new StringWriter();
      PrintWriter js = new PrintWriter(sw);

      if (!Modifier.isAbstract(codec.getBeanClass().getModifiers())
          && !Modifier.isInterface(codec.getBeanClass().getModifiers())) {

        js.println("(options) { ");
        js.flush();

        for (Field f : (Collection<Field>) codec.getFieldsByName().values()) {
          Codec fieldCodec = registry.getCodec(f.getType());
          js.print("  this." + f.getName() + " = ");
          Object defaultValue = codec.getDefaultValue(f);
          if (defaultValue == null) {
            js.print("null");
          } else {
            if (fieldCodec instanceof ArrayCodec
                || fieldCodec instanceof CollectionCodec
                || fieldCodec instanceof PrimitiveArrayCodec) {
              js.print("new Array()");
            } else if (fieldCodec instanceof BeanCodec) {
              js.print("new " + f.getType().getName() + "()");
            } else if (fieldCodec instanceof StringCodec) {
              js.print("\"" + defaultValue + "\"");
            } else if (fieldCodec instanceof CharacterCodec) {
              js.print("\"" + defaultValue + "\"");
            } else if (fieldCodec instanceof DateCodec) {
              Date date = (Date)defaultValue;
              long diff = date.getTime() - now.getTime();
              if (diff <0) {
                diff *= -1;
              }
              if (diff < 1000 * 60) {
                js.print("new Date()");
              }else {
                js.print("new Date(" + ((Date) defaultValue).getTime() +")");
              }
            } else if (fieldCodec instanceof EnumCodec) {
              js.print("\"" + defaultValue + "\"");
            } else {
              js.print(defaultValue);

            }
          }
          js.println(";");
          js.flush();
        }

        js.println("  for (var key in options) {");
        js.print("    ");
        for (Field f : (Collection<Field>) codec.getFieldsByName().values()) {
          js.println("if (key == \"" + f.getName() + "\") {");
          js.println("      this." + f.getName() + " = options[key];");
          js.print("    } else ");
        }
        js.println("{");
        js.println("      throw \"Unknown option key \"+ key + \" in "+codec.getBeanClass().getName()+"\";");
        js.println("    }");

        js.println("  }");
        // end constructor

        // serializer
        js.println("    this.serialize = function(){");

        boolean hasCollections = false;
        for (Field f : (Collection<Field>) codec.getFieldsByName().values()) {
          Codec fieldCodec = registry.getCodec(f.getType());
          if (fieldCodec instanceof CollectionCodec) {
            hasCollections = true;
          }
        }

        if (hasCollections) {
          js.println("      var i, length;");
        }
        js.println("      var json = '{ \"_class\" : \"" + codec.getBeanClass().getName() + "\"';");
        for (Field f : (Collection<Field>) codec.getFieldsByName().values()) {
          js.println("      if (this." + f.getName() + " != null)  {");
          Codec fieldCodec = registry.getCodec(f.getType());
          if (registry.getBeanCodecs().containsKey(f.getType())) {
            js.println("        json += ', \"" + f.getName() + "\" : ' + this." + f.getName() + ".serialize();");
          } else {
            if (fieldCodec instanceof CollectionCodec) {
              CollectionCodec collectionCodec = (CollectionCodec) fieldCodec;
              js.println("      for (i = 0, length = this." + f.getName() + ".length; i < length; i++) {");
              js.println("        if (i > 0) { json += ', '; }");
              Codec valueCodec = registry.getCodec(collectionCodec.getPrimitiveGenericType());
              if (valueCodec instanceof BeanCodec) {
                js.println("        json += this." + f.getName() + "[i].serialize();");
              } else if (valueCodec instanceof StringCodec) {
                js.println("      json += ', \"" + f.getName() + "\" : \"' + this." + f.getName() + "[i]+'\"';");
              } else if (valueCodec instanceof CharacterCodec) {
                js.println("      json += ', \"" + f.getName() + "\" : \"' + this." + f.getName() + "[i]+'\"';");
              } else if (valueCodec instanceof DateCodec) {
                js.println("      json += ', \"" + f.getName() + "\" : { \"_class\" : \"java.util.Date\", \"time\" : ' + this." + f.getName() + "[i].getTime()+' }';");
              } else {
                js.println("      json += ', \"" + f.getName() + "\" : ' + this." + f.getName() + "[i];");
              }
              js.println("      }");
            } else if (fieldCodec instanceof ArrayCodec) {
              ArrayCodec arrayCodec = (ArrayCodec) fieldCodec;
              js.println("      for (i = 0, length = this." + f.getName() + ".length; i < length; i++) {");
              js.println("        if (i > 0) { json += ', '; }");
              Codec valueCodec = registry.getCodec(arrayCodec.getPrimitiveGenericType());
              if (valueCodec instanceof BeanCodec) {
                js.println("        json += this." + f.getName() + "[i].serialize();");
              } else if (valueCodec instanceof StringCodec) {
                js.println("      json += ', \"" + f.getName() + "\" : \"' + this." + f.getName() + "[i]+'\"';");
              } else if (valueCodec instanceof CharacterCodec) {
                js.println("      json += ', \"" + f.getName() + "\" : \"' + this." + f.getName() + "[i]+'\"';");
              } else if (valueCodec instanceof DateCodec) {
                js.println("      json += ', \"" + f.getName() + "\" : { \"_class\" : \"java.util.Date\", \"time\" : ' + this." + f.getName() + "[i].getTime()+' }';");
              } else {
                js.println("      json += ', \"" + f.getName() + "\" : ' + this." + f.getName() + "[i];");
              }
              js.println("      }");
            } else if (fieldCodec instanceof PrimitiveArrayCodec) {
              PrimitiveArrayCodec arrayCodec = (PrimitiveArrayCodec) fieldCodec;
              js.println("      for (i = 0, length = this." + f.getName() + ".length; i < length; i++) {");
              js.println("        if (i > 0) { json += ', '; }");
              Codec valueCodec = registry.getCodec(arrayCodec.getPrimitiveGenericType());
              if (valueCodec instanceof BeanCodec) {
                js.println("        json += this." + f.getName() + "[i].serialize();");
              } else if (valueCodec instanceof StringCodec) {
                js.println("      json += ', \"" + f.getName() + "\" : \"' + this." + f.getName() + "[i]+'\"';");
              } else if (valueCodec instanceof CharacterCodec) {
                js.println("      json += ', \"" + f.getName() + "\" : \"' + this." + f.getName() + "[i]+'\"';");
              } else if (valueCodec instanceof DateCodec) {
                js.println("      json += ', \"" + f.getName() + "\" : { \"_class\" : \"java.util.Date\", \"time\" : ' + this." + f.getName() + "[i].getTime()+' }';");
              } else {
                js.println("      json += ', \"" + f.getName() + "\" : ' + this." + f.getName() + "[i];");
              }
              js.println("      }");

            } else if (fieldCodec instanceof MapCodec) {
              throw new UnsupportedOperationException("Maps not supported");


            } else if (fieldCodec instanceof StringCodec) {
              js.println("      json += ', \"" + f.getName() + "\" : \"' + this." + f.getName() + "+'\"';");
            } else if (fieldCodec instanceof CharacterCodec) {
              js.println("      json += ', \"" + f.getName() + "\" : \"' + this." + f.getName() + "+'\"';");
            } else if (fieldCodec instanceof DateCodec) {
              js.println("      json += ', \"" + f.getName() + "\" : { \"_class\" : \"java.util.Date\", \"time\" : ' + this." + f.getName() + ".getTime()+' }';");
            } else {
              js.println("      json += ', \"" + f.getName() + "\" : ' + this." + f.getName() + ";");
            }
          }
          js.println("      }");
          // end if not null
        }
        js.println("      json += ' }';");
        js.println("      return json;");
        js.println("    };");
        // end serializer


        js.println("};");
        // end class

        compiled.put(codec.getBeanClass(), sw.toString());

      }

      js.flush();

    }


    List<Map.Entry<Class, String>> ordered = new ArrayList<Map.Entry<Class, String>>(compiled.entrySet());
    Collections.sort(ordered, new Comparator<Map.Entry<Class, String>>() {
      @Override
      public int compare(Map.Entry<Class, String> classStringEntry, Map.Entry<Class, String> classStringEntry1) {
        return classStringEntry.getKey().getName().compareTo(classStringEntry1.getKey().getName());
      }
    });

    Map<List<String>, Set<Class>> packages = new HashMap<List<String>, Set<Class>>();
    for (Class type : compiled.keySet()) {
      List<String> path = Arrays.asList(type.getPackage().getName().split("\\."));
      for (int i = 1; i <= path.size(); i++) {
        List<String> pack = new ArrayList<String>(path.subList(0, i));
        Set<Class> classes = packages.get(pack);
        if (classes == null) {
          classes = new HashSet<Class>();
          packages.put(pack, classes);
        }
        classes.add(type);
      }
    }

    List<List<String>> orderedPackages = new ArrayList<List<String>>(packages.keySet());
    Collections.sort(orderedPackages, new Comparator<List<String>>() {
      @Override
      public int compare(List<String> strings, List<String> strings1) {
        int ret;
        for (int i = 0; i < strings.size() && i < strings1.size(); i++) {
          ret = strings.get(i).compareTo(strings1.get(i));
          if (ret != 0) {
            return ret;
          }
        }
        return strings.size() - strings1.size();
      }
    });

    Set<List<String>> defined = new HashSet<List<String>>();
    for (List<String> pack : orderedPackages) {
      for (int i = 0; i < pack.size(); i++) {
        List<String> path = pack.subList(0, i);
        if (defined.add(path)) {
          if (i == 0) {
            js.print("var ");
            js.print(pack.get(0));
          } else {
            for (int i2 = 0; i2 <= i; i2++) {
              if (i2 > 0) {
                js.print(".");
              }
              js.print(pack.get(i2));
            }
          }
          js.println(" = new Object();");
        }
      }
    }

    for (Map.Entry<Class, String> e : ordered) {
      js.print(e.getKey().getName());
      js.print(" = function");
      js.println(e.getValue());
      js.flush();
    }

    js.flush();

  }
}
