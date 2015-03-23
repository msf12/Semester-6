rm -rf foo bar fee

mkdir foo
mkdir -p bar/baz
mkdir -p fee/fie/foo/fum
touch foo/newfile.txt

mv foo/newfile.txt foo/touching.txt

cp foo/touching.txt fee/fie/touching-copy.txt

cat kt1.txt kt2.txt > bar/baz/kt-quotes.txt

cat random.txt | sort -n > fee/fie/foo/fum/sorted.txt

cat solutions.sh

echo ""

rm -rf foo bar fee