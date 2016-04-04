object sort {
	def quicksort[K](comparator:(K,K)=>Int)(input: List[K]): List[K] = {
		if (input.length <= 1) 
		input;
		else { 
			val pivot = input(input.length/2);
			List.concat(
				quicksort[K](comparator)(input.filter(comparator(_, pivot) > 0)),
				(input.filter(comparator(_, pivot) == 0)),
				quicksort[K](comparator)(input.filter(comparator(_, pivot) < 0)))
		}
	}

	def main(args: Array[String]): Unit = {
		println("generates 100 random integers from 1 to 1000 and quicksorts them")
		var list = List.tabulate(100)( _ => Math.ceil(Math.random() * 1000).toInt)
		var sorted_list = (quicksort((x: Int, y: Int) => {if (x < y) -1 else if (x > y) 1 else 0})(list))
		println(sorted_list)
	}
}