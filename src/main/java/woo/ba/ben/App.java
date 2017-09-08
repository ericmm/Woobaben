package woo.ba.ben;


public class App {

    public static void main(String[] args) throws Exception {
        Attr life = new Attr(1, "life", "D");
        Attr name = new Attr(2, "name", "S");
        Attr magic = new Attr(3, "magic", "D");
        Attr exp = new Attr(3, "exp", "D");


        Attr lifeGrowth = new Attr(8, "lifeGrowth", "D");
        Attr magicGrowth = new Attr(9, "magicGrowth", "D");

        AttrValue lifeValue = new AttrValue(2, life, new Double(100d));
        AttrValue nameValue = new AttrValue(3, name, "Test-name");
        AttrValue magicValue = new AttrValue(4, magic, new Double(100d));
        AttrValue expValue = new AttrValue(7, exp, new Double(98d));

        AttrValue lifeGrowthValue = new AttrValue(9, lifeGrowth, new Double(5d));
        AttrValue magicGrowthValue = new AttrValue(10, magicGrowth, new Double(10d));

        AttrValue[] instanceValues = new AttrValue[]{lifeValue, nameValue, magicValue, expValue, lifeGrowthValue, magicGrowthValue};

        EntityInstance instance = new EntityInstance(2, instanceValues);

//        Rule rule = new Rule(5, new Attr[]{life, magic}, "var_" + life.id + "  + sqrt( var_" + magic.id + ") ");
//
//        rule.variables[0].setValue((Double) lifeValue.value);
//        rule.variables[1].setValue((Double) magicValue.value);
//
//        System.out.println("result=" + rule.expression.evaluate());

//        Rule expRule = new Rule(7, new Attr[]{exp, }, "var_" + exp.id + "  + sqrt( var_" + magic.id + ") ");


//        double result = 0;
//        long start = System.currentTimeMillis();
//        for (int i = 0; i < 1_000_000_000; i++) {
//            result = 100d + sqrt(100d);
//        }
//        long end = System.currentTimeMillis();
//        System.out.println("it took " + (end - start));
//
//
//        start = System.currentTimeMillis();
//        for (int i = 0; i < 1_000_000_000; i++) {
//            result = rule.expression.evaluate();
//        }
//        end = System.currentTimeMillis();
//        System.out.println("it took " + (end - start));


//        ExpressionEvaluator ee = new ExpressionEvaluator();
//        ee.setParameters(new String[]{"a", "b"}, new Class[]{double.class, double.class});
//        ee.setExpressionType(double.class);
//        ee.cook(" a + Math.sqrt(b) ");
//
//        Double result1;
//        start = System.currentTimeMillis();
//        for (int i = 0; i < 1_000_000_000; i++) {
//            result1 = (Double) ee.evaluate(new Object[]{100d, 100d});
//        }
//        end = System.currentTimeMillis();
//        System.out.println("it took " + (end - start));
    }
}

class Attr {
    int id;
    String name;

    String type;

    Attr(int id, String name, String type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }
}

class AttrValue {
    int id;
    Attr attr;
    Object value;

    AttrValue(int id, Attr attr, Object value) {
        this.id = id;
        this.attr = attr;
        this.value = value;
    }
}

class EntityInstance {
    int id;
    AttrValue[] attrValues;

    EntityInstance(int id, AttrValue[] attrValues) {
        this.id = id;
        this.attrValues = attrValues;
    }

    Object getByAttrName(String attrName) {
        for (int i = 0; i < attrValues.length; i++) {
            if (attrName.equals(attrValues[i].attr.name)) {
                return attrValues[i].value;
            }
        }
        return null;
    }

}

class Rule {
    int id;
    String calculationExpr;
//    Scope scope;
//    Variable[] variables;
//    Expression expression;
//
//    Rule(int id, Attr[] relatedAttrs, String calculationExpr) throws ParseException {
//        this.id = id;
//        this.calculationExpr = calculationExpr;
//
//        scope = new Scope();
//        variables = new Variable[relatedAttrs.length];
//        for (int i = 0; i < relatedAttrs.length; i++) {
//            variables[i] = scope.create("var_" + relatedAttrs[i].id);
//        }
//        expression = Parser.parse(String.format(calculationExpr, variables), scope);
//    }
}