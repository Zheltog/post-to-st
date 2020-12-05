/**
 * generated by Xtext 2.23.0
 */
package su.nsk.iae.post.poST;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Numeric Literal</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link su.nsk.iae.post.poST.NumericLiteral#getType <em>Type</em>}</li>
 * </ul>
 *
 * @see su.nsk.iae.post.poST.PoSTPackage#getNumericLiteral()
 * @model
 * @generated
 */
public interface NumericLiteral extends Constant
{
  /**
   * Returns the value of the '<em><b>Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Type</em>' attribute.
   * @see #setType(String)
   * @see su.nsk.iae.post.poST.PoSTPackage#getNumericLiteral_Type()
   * @model
   * @generated
   */
  String getType();

  /**
   * Sets the value of the '{@link su.nsk.iae.post.poST.NumericLiteral#getType <em>Type</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Type</em>' attribute.
   * @see #getType()
   * @generated
   */
  void setType(String value);

} // NumericLiteral