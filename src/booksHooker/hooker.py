import requests
import os
from random import randint
from alive_progress import alive_bar

# Save picture
# 0 -> already exist
# 1 -> created on this call
def saveImage(image_url,fileName):
    imgData = requests.get("http://books.google.com/books/"+image_url).content
    try:
        with open(fileName):
            return 0
    except IOError:
        with open(fileName, 'wb') as handler:
            handler.write(imgData)
        return 1

def buildBookLigne(id,titre,auteur,prix,synopsis,edition,Date,editeur,urlImage,stock):
    return str(id)+';"'+cleanStr(titre)+'";"'+cleanStr(auteur)+'";'+str(prix)+';"'+cleanStr(synopsis)+'";'+str(edition)+';'+str(Date)+';"'+cleanStr(editeur)+'";"'+cleanStr(urlImage)+'";'+str(stock)+'\n'

def cleanStr(string):
    return string.replace('"',"'")

def saveBook(id,titre,auteur,prix,synopsis,edition,Date,editeur,urlImage,stock):
    lineBook = buildBookLigne(id,titre,auteur,prix,synopsis,edition,Date,editeur,urlImage,stock)
    bookFile.write(lineBook)

def saveCategory(dictionaryCategories,categorie,bookID):
    if categorie not in dictionaryCategories :
        #creation
        idNewCat = len(dictionaryCategories) + 1
        dictionaryCategories[categorie] = idNewCat
        categorieFile.write(str(dictionaryCategories[categorie])+';'+categorie+';"default description"\n')
    livre_catFile.write(str(bookID)+';'+str(dictionaryCategories[categorie])+'\n')

def hookBook():
    # init const
    pageSize = 40

    # init var
    status = 200
    id=1


    # init struct
    keysWords=["double","dranse","étoile","développement","étude","épique"]
    dictionaryCategories = dict()

    # hook data books
    for keysWord in keysWords :
        status = 200
        nbFind = 0
        nbErrors = 0
        with alive_bar(200) as bar:
            bar.text(keysWord)
            while status == 200 and nbFind < 200 :
                url = "https://www.googleapis.com/books/v1/volumes?q="+keysWord+"&startIndex="+str(nbFind+nbErrors)+"&maxResults="+str(pageSize)+"&projection=full&full&books&langRestrict=fr"
                try :
                    result = requests.get(url)
                    result.raise_for_status()
                    res =result.json()
                    books = res['items']
                except requests.exceptions.HTTPError as errh:
                    status = 400
                    break
                except requests.exceptions.ConnectionError as errc:
                    status = 400
                    break
                except requests.exceptions.Timeout as errt:
                    status = 400
                    break
                except requests.exceptions.RequestException as err:
                    status = 400
                    break
                except requests.ConnectionError as error:
                    status = 400
                    break
                except KeyError :
                    status = 400
                    break
                for book in books:
                    try :
                        # try to get data
                        titre     = book['volumeInfo']['title']
                        auteur    = book['volumeInfo']['authors'][0]
                        categorie = book['volumeInfo']['categories'][0]
                        prix      = book['saleInfo']['listPrice']['amount']
                        synopsis  = book['volumeInfo']['description']
                        editeur   = book['volumeInfo']['publisher']
                        Date      = book['volumeInfo']['publishedDate'][0:4]
                        edition   = 1
                        urlImage  = (book['volumeInfo']['imageLinks']['thumbnail']).split('/')[-1]
                        stock     = randint(0,25) * randint(1,5)
                        nameImage = (urlImage.split('=')[1].split('&'))[0].lower()+'.jpeg'
                        # save data
                        saveBook(id,titre,auteur,prix,synopsis,edition,Date,editeur,nameImage,stock)
                        saveCategory(dictionaryCategories,categorie,id)
                        saveImage(urlImage,os.path.join('data/pictures',nameImage))
                        nbFind+=1
                        id+=1
                        bar()
                        if nbFind == 200 :
                            break
                    except KeyError as err:
                        nbErrors+=1
def main():
    hookBook()

dataDir="data"
bookFile = open(os.path.join(dataDir,'livre.csv'),'w')
categorieFile = open(os.path.join(dataDir,'categorie.csv'),'w')
livre_catFile = open(os.path.join(dataDir,'livre_cat.csv'),'w')
bookFile.write('id;titre;auteur;prix;synopsis;edition;annee_publication;editeur;url_image;stock\n')
categorieFile.write('id;nom;description\n')
livre_catFile.write('livre_id;livre_cat_id\n')

if __name__ == "__main__":
    main()
bookFile.close()
categorieFile.close()
livre_catFile.close()





