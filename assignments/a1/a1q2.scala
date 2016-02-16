object sort{
	def quicksort(input: List[Int]): List[Int] = {
		if (input.length <= 1) input
		else {
			val pivot = input(input.length / 2)
			List.concat(
				quicksort(input filter (pivot >)),
				input filter (pivot ==),
				quicksort(input filter (pivot <)))
		}
	}



	def main(args: Array[String]): Unit = {
		println("welcome to quicksort")
		var list = List.tabulate(50)( _ => (Math.random*10).toInt )
		println(quicksort(list))
	}
}
