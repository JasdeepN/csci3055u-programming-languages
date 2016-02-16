object person{
	def main(args: Array[String]): Unit = {
		var(age,name, grow)=makePerson("Clark Kent",28);
		println(age())
		println(name())
		println(grow()) //29
		println(grow()) //30
		println(grow()) //31
	}

	def makePerson(n: String, a: Int): (() => Int, () => String, () => Int) = {
		var temp = 0
		val name: () => String = () => {n}
		var age: () => Int = () => {a}
		var grow = () => {
			temp = temp + 1
			a+temp}
		(age, name, grow)	
	}
}