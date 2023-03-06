// Own code ->

package Harkka2020;

class DynArrStack {

	// Taulukon koko
	private int N;
	// Taulukko
	private Object[] A;
	// Pinossa olevien objektien lukumäärä.
	private int n;

	// Rakentaja
	public DynArrStack() {
		N = 1;
		n = 0;
		A = new Object[N];
	}

	public boolean isEmpty() {
		return n == 0;
	}

	public int arraySize() {
		return N;
	}

	// Lisää pinon päälle objektin. Tarvittaessa taulukon koko tuplataan.
	public void push( Object x ) {
		if( n == N ) {
			
			N = 2*N;

			Object[] B = new Object[N];

			for( int i = 0; i < n; i++ ) {
				B[i] = A[i];
				A[i] = null;
			}

			A = B;
			B = null;
		}

		A[n++] = x;
	}


	// Poistaa pinosta päällimmäisen objektin. Tarvittaessa taulukon koko puolitetaan.
	public Object pop () {

		Object result = null;

		if( !isEmpty() ) {

			result = A[--n];
			A[n] = null;

			if( n == (N/4) && (N >= 2) ) {

				N = N/2;

				Object[] B = new Object[N];

				for( int i = 0; i < n; i++ ) {
					B[i] = A[i];
					A[i] = null;
				}

				A = B;
				B = null;
			}
		}
		return result;
	}
}
// <- Own code