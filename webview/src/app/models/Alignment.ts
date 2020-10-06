export enum Alignment {
  Left,
  Center,
  Right
}

export function alignmentToAlignmentString(alignment: Alignment): string {
  switch (alignment) {
    case Alignment.Left:
      return 'left';
    case Alignment.Center:
      return 'center';
    case Alignment.Right:
      return 'right';
    default:
      return null;
  }
}
