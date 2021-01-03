package au.id.tmm.intime

package object scalacheck
    extends ArbitraryInstances
    with ChooseInstances
    with CogenInstances
    with SensibleJavaTimeGenerators
    with ShrinkInstances {
  object year extends YearInstances

  object all extends AnyRef with YearInstances
}
