export enum FontFamily {
  Roboto = 'ROBOT',
  OpenSans = 'OPEN_SANS',
  Montserrat = 'MONTSERRAT',
  PlayfairDisplay = 'PLAYFAIR_DISPLAY'

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
