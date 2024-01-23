
export interface Product {
  cat_id: string
  price: string
  prod_describe: string
  prod_id: string
  prod_image: string
  prod_name: string
  // prod_quantity: string
}


export interface CreateProduct {
  title: string
  price: string
  description: string
  brand: string
  category: string
  color: string
  images: string[]
}