package pdb.contacts;

import pair.Pair;
import pdb.structures.Residue;
import pdb.structures.SpacePoint;

public abstract class ContactCriteria {

  public abstract Pair<SpacePoint, SpacePoint> areInContact(
      Residue currentFirstPoint, Residue currentSecondPoint);

  public abstract int cost();

}
