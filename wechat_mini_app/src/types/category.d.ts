import type { DishItem } from './dish'
import type { SetmealItem } from '@/types/setmeal'

// 分类列表
export type CategoryItem = {
  children: (DishItem | SetmealItem)[]
  id: number
  name: string
  sort: number
  type: number
}
