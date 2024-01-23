export const handlePrice = (price: number): string => {
    return new Intl.NumberFormat('de-DE', {
        minimumFractionDigits: 0,
        maximumFractionDigits: 0
    }).format(price);
};

export const formatCurrencyVND = (amount: string): string => {
  const formatter = new Intl.NumberFormat("vi-VN", {
    style: "currency",
    currency: "VND",
  })

  return formatter.format(Number(amount))
}