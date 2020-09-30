export enum FontFamily {
  Roboto,
  OpenSans,
  Montserrat,
  PlayfairDisplay

}

export function fontFamilyToFontString(font: FontFamily): string {
    switch (font) {
      case FontFamily.Roboto:
        return 'Roboto';
      case FontFamily.OpenSans:
        return 'Open Sans';
      case FontFamily.Montserrat:
        return 'Montserrat';
      case FontFamily.PlayfairDisplay:
        return 'Playfair Display';
      default:
        return null;
    }
}
