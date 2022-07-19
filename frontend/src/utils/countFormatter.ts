const countFormatter = (count: number): string => {
  const format = [
    { unit: 1e3, abbreviation: 'K' },
    { unit: 1e6, abbreviation: 'M' },
    { unit: 1e9, abbreviation: 'B' },
    { unit: 1e12, abbreviation: 'T' },
    { unit: 1e15, abbreviation: 'P' },
    { unit: 1e18, abbreviation: 'E' },
  ];

  if (count < 1e3) return String(count);

  const { unit, abbreviation } = format.reverse().find(format => count >= format.unit)!;

  return parseFloat((count / unit).toFixed(2)) + abbreviation;
};

export default countFormatter;
