export function isAtLeast14YearsOld(birthDate: string, givenDate: string): boolean {
  const TEENAGER_AGE = 14;

  const given = givenDate ? new Date(givenDate) : new Date();
  const birth = new Date(birthDate);

  const age = given.getFullYear() - birth.getFullYear();
  const monthDifference = given.getMonth() - birth.getMonth();
  const dayDifference = given.getDate() - birth.getDate();

  return (
    age > TEENAGER_AGE ||
    (age === TEENAGER_AGE && (monthDifference > 0 || (monthDifference === 0 && dayDifference >= 0)))
  );
}
