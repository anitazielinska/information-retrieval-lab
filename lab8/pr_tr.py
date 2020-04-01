import numpy as np

L1  = [0, 1, 1, 0, 1, 0, 0, 0, 0, 0]
L2  = [1, 0, 0, 1, 0, 0, 0, 0, 0, 0]
L3  = [0, 1, 0, 0, 0, 0, 1, 0, 0, 0]
L4  = [0, 1, 1, 0, 0, 0, 0, 0, 0, 0]
L5  = [0, 0, 0, 0, 0, 1, 1, 0, 0, 0]
L6  = [0, 0, 0, 0, 0, 0, 1, 1, 0, 0]
L7  = [0, 0, 0, 0, 1, 1, 1, 1, 1, 1]
L8  = [0, 0, 0, 0, 0, 0, 1, 0, 1, 0]
L9  = [0, 0, 0, 0, 0, 0, 1, 0, 0, 1]
L10 = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0]

L = np.array([L1, L2, L3, L4, L5, L6, L7, L8, L9, L10])

ITERATIONS = 100

def getM(L):
    M = np.zeros([10, 10], dtype=float)
    # number of outgoing links
    c = np.zeros([10], dtype=int)
    
    for i in range(0, 10):
        c[i] = sum(L[i])
    
    for i in range(0, 10):
        for j in range(0, 10):
            if L[j][i] == 0:
                M[i][j] = 0
            else:
                M[i][j] = 1.0/c[j]
    return M


def getPageRank(M, damping_factor = 0.15):
    R = [1.0/len(M) for i in M]
    Rc = R[:]

    # Calculating PageRank
    for iteration in range(ITERATIONS):
        for rowi, row in enumerate(M):
            summing = 0
            for vali, val in enumerate(row):
                summing += val*R[vali]
            Rc[rowi] = damping_factor + (1.0-damping_factor)*summing

        R = Rc[:]

    # Normalizing
    rsum = sum(R)
    R = [i/rsum for i in R]

    # Sorting and creating answer list
    Res = list(range(len(R)))
    for i, val in enumerate(R):
        Res[i] = [i+1, val]
    Res.sort(key = lambda val: -val[1])

    return Res


def getTrustRank(M, trusted, damping_factor = 0.15):
    sumtrust = sum(trusted)
    trust = [i/sumtrust for i in trusted]
    trustc = trust[:]

    # Calculating TrustRank
    for iteration in range(ITERATIONS):
        for rowi, row in enumerate(M):
            summing = 0
            for vali, val in enumerate(row):
                summing += val*trust[vali]
            trustc[rowi] = damping_factor*trust[rowi] + (1.0-damping_factor)*summing

        trust = trustc[:]

    # Normalizing
    trustsum = sum(trust)
    trust = [i/trustsum for i in trust]

    # Sorting and creating answer list
    Res = list(range(len(trust)))
    for i, val in enumerate(trust):
        Res[i] = [i+1, val]
    Res.sort(key = lambda val: -val[1])

    return Res


print("Matrix L (indices)")
print(L)

M = getM(L)

print("Matrix M (stochastic matrix)")
print(M)

#------- 2 -----------
print("PAGERANK")

q = 0.15

pr = getPageRank(M, q)
for i in pr:
    print(i)

#------- 3 -----------
print("TRUSTRANK (DOCUMENTS 1 AND 2 ARE GOOD)")

q = 0.15

tr = getTrustRank(M, [1,1,0,0,0,0,0,0,0,0], q)

for i in tr:
    print(i)
    
#------- 4 -----------
print("TRUSTRANK (REMOVED CONNECTIONS 3->7, 1->5)")

L1  = [0, 1, 1, 0, 0, 0, 0, 0, 0, 0]
L2  = [1, 0, 0, 1, 0, 0, 0, 0, 0, 0]
L3  = [0, 1, 0, 0, 0, 0, 0, 0, 0, 0]
L4  = [0, 1, 1, 0, 0, 0, 0, 0, 0, 0]
L5  = [0, 0, 0, 0, 0, 1, 1, 0, 0, 0]
L6  = [0, 0, 0, 0, 0, 0, 1, 1, 0, 0]
L7  = [0, 0, 0, 0, 1, 1, 1, 1, 1, 1]
L8  = [0, 0, 0, 0, 0, 0, 1, 0, 1, 0]
L9  = [0, 0, 0, 0, 0, 0, 1, 0, 0, 1]
L10 = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0]

L = np.array([L1, L2, L3, L4, L5, L6, L7, L8, L9, L10])

M = getM(L)

q = 0.15

tr = getTrustRank(M, [1,1,0,0,0,0,0,0,0,0], q)

for i in tr:
    print(i)
