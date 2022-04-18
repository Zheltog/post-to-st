package su.nsk.iae.post.generator.plcopen.xml.common.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import su.nsk.iae.post.generator.plcopen.xml.common.ProcessGenerator;
import su.nsk.iae.post.generator.plcopen.xml.common.vars.VarHelper;
import su.nsk.iae.post.generator.plcopen.xml.common.vars.data.VarData;
import su.nsk.iae.post.poST.AddExpression;
import su.nsk.iae.post.poST.AndExpression;
import su.nsk.iae.post.poST.ArrayVariable;
import su.nsk.iae.post.poST.CompExpression;
import su.nsk.iae.post.poST.Constant;
import su.nsk.iae.post.poST.EquExpression;
import su.nsk.iae.post.poST.EquOperator;
import su.nsk.iae.post.poST.Expression;
import su.nsk.iae.post.poST.FunctionCall;
import su.nsk.iae.post.poST.IntegerLiteral;
import su.nsk.iae.post.poST.MulExpression;
import su.nsk.iae.post.poST.MulOperator;
import su.nsk.iae.post.poST.NumericLiteral;
import su.nsk.iae.post.poST.ParamAssignment;
import su.nsk.iae.post.poST.ParamAssignmentElements;
import su.nsk.iae.post.poST.PowerExpression;
import su.nsk.iae.post.poST.PrimaryExpression;
import su.nsk.iae.post.poST.ProcessStatusExpression;
import su.nsk.iae.post.poST.RealLiteral;
import su.nsk.iae.post.poST.SignedInteger;
import su.nsk.iae.post.poST.SymbolicVariable;
import su.nsk.iae.post.poST.TimeLiteral;
import su.nsk.iae.post.poST.UnaryExpression;
import su.nsk.iae.post.poST.XorExpression;

@SuppressWarnings("all")
public class GeneratorUtil {
  public static String generateStopConstant() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("_STOP");
    return _builder.toString();
  }
  
  public static String generateErrorConstant() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("_ERROR");
    return _builder.toString();
  }
  
  public static String generateGlobalTime() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("_global_time");
    return _builder.toString();
  }
  
  public static String generateVarName(final String process, final String variable) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("_p_�process�_v_�variable�");
    return _builder.toString();
  }
  
  public static String generateVarName(final ProcessGenerator process, final String variable) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("_p_�process.name�_v_�variable�");
    return _builder.toString();
  }
  
  public static String generateTimeoutName(final ProcessGenerator process) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("_g_p_�process.name�_time");
    return _builder.toString();
  }
  
  public static String generateEnumName(final ProcessGenerator process) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("_g_p_�process.name�_state");
    return _builder.toString();
  }
  
  public static String generateEnumStateConstant(final ProcessGenerator process, final String name) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("_P_�process.name.toUpperCase�_S_�name.toUpperCase�");
    return _builder.toString();
  }
  
  public static String generateConstant(final Constant constant) {
    NumericLiteral _num = constant.getNum();
    boolean _tripleNotEquals = (_num != null);
    if (_tripleNotEquals) {
      final NumericLiteral num = constant.getNum();
      if ((num instanceof IntegerLiteral)) {
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("�IF num.type !== null��num.type�#�ENDIF��num.value.generateSignedInteger�");
        return _builder.toString();
      }
      final RealLiteral dNum = ((RealLiteral) num);
      StringConcatenation _builder_1 = new StringConcatenation();
      _builder_1.append("�IF dNum.type !== null��dNum.type�#�ENDIF��IF dNum.RSig�-�ENDIF��dNum.value�");
      return _builder_1.toString();
    }
    TimeLiteral _time = constant.getTime();
    boolean _tripleNotEquals_1 = (_time != null);
    if (_tripleNotEquals_1) {
      StringConcatenation _builder_2 = new StringConcatenation();
      _builder_2.append("T#�constant.time.interval�");
      return _builder_2.toString();
    }
    return constant.getOth();
  }
  
  public static String generateSignedInteger(final SignedInteger sint) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("�IF sint.ISig�-�ENDIF��sint.value�");
    return _builder.toString();
  }
  
  public static String generateVars(final VarHelper varHelper) {
    return GeneratorUtil.generateVars(varHelper, null);
  }
  
  public static String generateVars(final VarHelper varHelper, final String name) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("�IF !varHelper.list.empty�");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("�IF varHelper.hasConstant�");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("<�varHelper.type��IF name !== null� name=\"�name�\"�ENDIF� constant=\"true\">");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("�FOR v : varHelper.list�");
    _builder.newLine();
    _builder.append("\t\t\t\t");
    _builder.append("�IF v.isConstant�");
    _builder.newLine();
    _builder.append("\t\t\t\t\t");
    _builder.append("�v.generateSingleDeclaration�");
    _builder.newLine();
    _builder.append("\t\t\t\t");
    _builder.append("�ENDIF�");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("�ENDFOR�");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("</�varHelper.type�>");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("�ENDIF�");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("�IF varHelper.hasNonConstant�");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("<�varHelper.type��IF name !== null� name=\"�name�\"�ENDIF�>");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("�FOR v : varHelper.list�");
    _builder.newLine();
    _builder.append("\t\t\t\t");
    _builder.append("�IF !v.isConstant�");
    _builder.newLine();
    _builder.append("\t\t\t\t\t");
    _builder.append("�v.generateSingleDeclaration�");
    _builder.newLine();
    _builder.append("\t\t\t\t");
    _builder.append("�ENDIF�");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("�ENDFOR�");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("</�varHelper.type�>");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("�ENDIF�");
    _builder.newLine();
    _builder.append("�ENDIF�");
    _builder.newLine();
    return _builder.toString();
  }
  
  private static String generateSingleDeclaration(final VarData data) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<variable name=\"�data.name�\">");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("<type>");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("�IF !data.isArray�");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("<�data.type� />");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("�ELSE�");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("<array>");
    _builder.newLine();
    _builder.append("\t\t\t\t");
    _builder.append("<dimension lower=\"�data.arrayStart�\" upper=\"�data.arrayEnd�\" />");
    _builder.newLine();
    _builder.append("\t\t\t\t");
    _builder.append("<baseType>");
    _builder.newLine();
    _builder.append("\t\t\t\t\t");
    _builder.append("<�data.type� />");
    _builder.newLine();
    _builder.append("\t\t\t\t");
    _builder.append("</baseType>");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("</array>");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("�ENDIF�");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("</type>");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("�IF data.value !== null�");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("<initialValue>");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("<simpleValue value=\"�data.value�\" />");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("</initialValue>");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("�ENDIF�");
    _builder.newLine();
    _builder.append("</variable>");
    _builder.newLine();
    return _builder.toString();
  }
  
  public static String generateXMLStart() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
    _builder.newLine();
    _builder.append("<project xmlns=\"http://www.plcopen.org/xml/tc6_0200\">");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("<fileHeader companyName=\"\" productName=\"poSTIDE\" productVersion=\"\" creationDateTime=\"�generateCurrentTime�\" />");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("<contentHeader name=\"poST.project\">");
    _builder.newLine();
    _builder.append("\t\t ");
    _builder.append("<coordinateInfo>");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("<fbd>");
    _builder.newLine();
    _builder.append("\t\t\t\t");
    _builder.append("<scaling x=\"1\" y=\"1\" />");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("</fbd>");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("<ld>");
    _builder.newLine();
    _builder.append("\t\t\t\t");
    _builder.append("<scaling x=\"1\" y=\"1\" />");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("</ld>");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("<sfc>");
    _builder.newLine();
    _builder.append("\t\t\t\t");
    _builder.append("<scaling x=\"1\" y=\"1\" />");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("</sfc>");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("</coordinateInfo>");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("</contentHeader>");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("<types>");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("<dataTypes />");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("<pous>");
    _builder.newLine();
    return _builder.toString();
  }
  
  public static String generateXMLEnd() {
    return GeneratorUtil.generateXMLEndWithGlobalVars(null);
  }
  
  public static String generateXMLEndWithGlobalVars(final VarHelper globalVars) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("\t\t");
    _builder.append("</pous>");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("</types>");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("<instances>");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("<configurations />");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("</instances>");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("�IF globalVars !== null�");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("<addData>");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("<data name=\"http://www.3s-software.com/plcopenxml/globalvars\" handleUnknown=\"implementation\">");
    _builder.newLine();
    _builder.append("\t\t\t\t");
    _builder.append("�generateGlobalVars(globalVars, \"GVL\")�");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("</data>");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("</addData>");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("�ENDIF�");
    _builder.newLine();
    _builder.append("</project>");
    _builder.newLine();
    return _builder.toString();
  }
  
  private static String generateGlobalVars(final VarHelper varHelper, final String name) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("�IF !varHelper.list.empty�");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("<globalVars name=\"�name�\">");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("�FOR v : varHelper.list�");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("�IF v.isConstant�");
    _builder.newLine();
    _builder.append("\t\t\t\t");
    _builder.append("�v.generateSingleDeclaration�");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("�ENDIF���");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("�ENDFOR�");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("�IF varHelper.hasConstant�");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("<addData>");
    _builder.newLine();
    _builder.append("\t\t\t\t");
    _builder.append("<data name=\"http://www.3s-software.com/plcopenxml/mixedattrsvarlist\" handleUnknown=\"implementation\">");
    _builder.newLine();
    _builder.append("\t\t\t\t\t");
    _builder.append("<MixedAttrsVarList>");
    _builder.newLine();
    _builder.append("\t\t\t\t\t\t");
    _builder.append("�varHelper.generateVars(name)�");
    _builder.newLine();
    _builder.append("\t\t\t\t\t");
    _builder.append("</MixedAttrsVarList>");
    _builder.newLine();
    _builder.append("\t\t\t\t");
    _builder.append("</data>");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("</addData>");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("�ENDIF�");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("</globalVars>");
    _builder.newLine();
    _builder.append("�ENDIF�");
    _builder.newLine();
    return _builder.toString();
  }
  
  private static String generateCurrentTime() {
    return new SimpleDateFormat("yyyy-MM-dd\'T\'HH:mm:ss.SSSSSS").format(Calendar.getInstance().getTime());
  }
  
  public static String generateExpression(final Expression exp) {
    return GeneratorUtil.generateExpression(exp, null, null, null);
  }
  
  public static String generateExpression(final Expression exp, final Function<SymbolicVariable, String> gVar, final Function<ArrayVariable, String> gArray, final Function<ProcessStatusExpression, String> gPStatus) {
    boolean _matched = false;
    if (exp instanceof PrimaryExpression) {
      _matched=true;
      Constant _const = ((PrimaryExpression)exp).getConst();
      boolean _tripleNotEquals = (_const != null);
      if (_tripleNotEquals) {
        return GeneratorUtil.generateConstant(((PrimaryExpression)exp).getConst());
      } else {
        SymbolicVariable _variable = ((PrimaryExpression)exp).getVariable();
        boolean _tripleNotEquals_1 = (_variable != null);
        if (_tripleNotEquals_1) {
          if ((gVar != null)) {
            return gVar.apply(((PrimaryExpression)exp).getVariable());
          }
          return ((PrimaryExpression)exp).getVariable().getName();
        } else {
          ArrayVariable _array = ((PrimaryExpression)exp).getArray();
          boolean _tripleNotEquals_2 = (_array != null);
          if (_tripleNotEquals_2) {
            if ((gArray != null)) {
              return gArray.apply(((PrimaryExpression)exp).getArray());
            }
            StringConcatenation _builder = new StringConcatenation();
            _builder.append("�exp.array.variable.name�[�exp.array.index.generateExpression(gVar, gArray, gPStatus)�]");
            return _builder.toString();
          } else {
            ProcessStatusExpression _procStatus = ((PrimaryExpression)exp).getProcStatus();
            boolean _tripleNotEquals_3 = (_procStatus != null);
            if (_tripleNotEquals_3) {
              if ((gPStatus != null)) {
                return gPStatus.apply(((PrimaryExpression)exp).getProcStatus());
              }
              StringConcatenation _builder_1 = new StringConcatenation();
              return _builder_1.toString();
            } else {
              FunctionCall _funCall = ((PrimaryExpression)exp).getFunCall();
              boolean _tripleNotEquals_4 = (_funCall != null);
              if (_tripleNotEquals_4) {
                StringConcatenation _builder_2 = new StringConcatenation();
                _builder_2.append("�exp.funCall.function.name�(�exp.funCall.args.generateParamAssignmentElements([x | x.generateExpression(gVar, gArray, gPStatus)])�)");
                return _builder_2.toString();
              } else {
                StringConcatenation _builder_3 = new StringConcatenation();
                _builder_3.append("(�exp.nestExpr.generateExpression(gVar, gArray, gPStatus)�)");
                return _builder_3.toString();
              }
            }
          }
        }
      }
    }
    if (!_matched) {
      if (exp instanceof UnaryExpression) {
        _matched=true;
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("�IF exp.unOp == UnaryOperator.NOT�NOT �ELSEIF exp.unOp == UnaryOperator.UNMINUS�-�ENDIF��exp.right.generateExpression(gVar, gArray, gPStatus)�");
        return _builder.toString();
      }
    }
    if (!_matched) {
      if (exp instanceof PowerExpression) {
        _matched=true;
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("�exp.left.generateExpression(gVar, gArray, gPStatus)� ** �exp.right.generateExpression(gVar, gArray, gPStatus)�");
        return _builder.toString();
      }
    }
    if (!_matched) {
      if (exp instanceof MulExpression) {
        _matched=true;
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("�exp.left.generateExpression(gVar, gArray, gPStatus)� �exp.mulOp.generateMulOperators� �exp.right.generateExpression(gVar, gArray, gPStatus)�");
        return _builder.toString();
      }
    }
    if (!_matched) {
      if (exp instanceof AddExpression) {
        _matched=true;
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("�exp.left.generateExpression(gVar, gArray, gPStatus)� �IF exp.addOp == AddOperator.PLUS�+�ELSE�-�ENDIF� �exp.right.generateExpression(gVar, gArray, gPStatus)�");
        return _builder.toString();
      }
    }
    if (!_matched) {
      if (exp instanceof EquExpression) {
        _matched=true;
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("�exp.left.generateExpression(gVar, gArray, gPStatus)� �exp.equOp.generateEquOperators� �exp.right.generateExpression(gVar, gArray, gPStatus)�");
        return _builder.toString();
      }
    }
    if (!_matched) {
      if (exp instanceof CompExpression) {
        _matched=true;
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("�exp.left.generateExpression(gVar, gArray, gPStatus)� �IF exp.compOp == CompOperator.EQUAL�=�ELSE�<>�ENDIF� �exp.right.generateExpression(gVar, gArray, gPStatus)�");
        return _builder.toString();
      }
    }
    if (!_matched) {
      if (exp instanceof AndExpression) {
        _matched=true;
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("�exp.left.generateExpression(gVar, gArray, gPStatus)� AND �exp.right.generateExpression(gVar, gArray, gPStatus)�");
        return _builder.toString();
      }
    }
    if (!_matched) {
      if (exp instanceof XorExpression) {
        _matched=true;
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("�exp.left.generateExpression(gVar, gArray, gPStatus)� XOR �exp.right.generateExpression(gVar, gArray, gPStatus)�");
        return _builder.toString();
      }
    }
    if (!_matched) {
      if (exp instanceof Expression) {
        _matched=true;
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("�exp.left.generateExpression(gVar, gArray, gPStatus)� OR �exp.right.generateExpression(gVar, gArray, gPStatus)�");
        return _builder.toString();
      }
    }
    return null;
  }
  
  public static String generateParamAssignmentElements(final ParamAssignmentElements elements) {
    final Function<Expression, String> _function = (Expression x) -> {
      return GeneratorUtil.generateExpression(x);
    };
    return GeneratorUtil.generateParamAssignmentElements(elements, _function);
  }
  
  public static String generateParamAssignmentElements(final ParamAssignmentElements elements, final Function<Expression, String> gExp) {
    final Function<ParamAssignment, String> _function = (ParamAssignment x) -> {
      return GeneratorUtil.generateParamAssignment(x, gExp);
    };
    return IterableExtensions.join(elements.getElements().stream().<String>map(_function).collect(Collectors.<String>toList()), ", ");
  }
  
  private static String generateParamAssignment(final ParamAssignment ele, final Function<Expression, String> gExp) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("�ele.variable.name� �IF ele.assig == AssignmentType.IN�:=�ELSE�=>�ENDIF� �gExp.apply(ele.value)�");
    return _builder.toString();
  }
  
  private static String generateEquOperators(final EquOperator op) {
    if (op != null) {
      switch (op) {
        case LESS:
          StringConcatenation _builder = new StringConcatenation();
          _builder.append("<");
          return _builder.toString();
        case LESS_EQU:
          StringConcatenation _builder_1 = new StringConcatenation();
          _builder_1.append("<=");
          return _builder_1.toString();
        case GREATER:
          StringConcatenation _builder_2 = new StringConcatenation();
          _builder_2.append(">");
          return _builder_2.toString();
        case GREATER_EQU:
          StringConcatenation _builder_3 = new StringConcatenation();
          _builder_3.append(">=");
          return _builder_3.toString();
        default:
          break;
      }
    }
    return null;
  }
  
  private static String generateMulOperators(final MulOperator op) {
    if (op != null) {
      switch (op) {
        case MUL:
          StringConcatenation _builder = new StringConcatenation();
          _builder.append("*");
          return _builder.toString();
        case DIV:
          StringConcatenation _builder_1 = new StringConcatenation();
          _builder_1.append("/");
          return _builder_1.toString();
        case MOD:
          StringConcatenation _builder_2 = new StringConcatenation();
          _builder_2.append("MOD");
          return _builder_2.toString();
        default:
          break;
      }
    }
    return null;
  }
}
