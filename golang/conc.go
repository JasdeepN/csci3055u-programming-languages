package main

import(
	"fmt"
	"time"
)

type WebPage struct {
	URL 	string
	Content string
}

func NewWebPage(url, content string) *WebPage {
	return &WebPage{url, content}
}

func (page *WebPage) String() string {
	return fmt.Sprintf("[%s] %s", page.URL, page.Content)
}

func GET(url string) *WebPage{
	time.Sleep(3 * time.Second)
	return NewWebPage(url, "Tesla Model III")
}

// func main() {
// 	var page = GET("google.com")
// 	fmt.Println(page)
// }

func main() {
	var webpage = make(chan *WebPage)
	var url = "www.google.com/news"
	go func() {
		webpage <- GET(url)
	}()
	fmt.Println("I am working on it")
	fmt.Println("Got it: ", <-webpage)
}
